package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.dto.CommentsBackDTO;
import com.iksling.blog.dto.CommentsDTO;
import com.iksling.blog.dto.CommentsReplyDTO;
import com.iksling.blog.entity.Article;
import com.iksling.blog.entity.Comment;
import com.iksling.blog.exception.IllegalRequestException;
import com.iksling.blog.exception.OperationStatusException;
import com.iksling.blog.mapper.ArticleMapper;
import com.iksling.blog.mapper.CommentMapper;
import com.iksling.blog.pojo.Condition;
import com.iksling.blog.pojo.LoginUser;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.service.CommentService;
import com.iksling.blog.util.IpUtil;
import com.iksling.blog.util.RegexUtil;
import com.iksling.blog.util.UserUtil;
import com.iksling.blog.vo.CommentVO;
import com.iksling.blog.vo.StatusBackVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

import static com.iksling.blog.constant.FlagConst.DELETED;
import static com.iksling.blog.constant.FlagConst.RECYCLE;
import static com.iksling.blog.constant.RedisConst.COMMENT_LIKE_COUNT;
import static com.iksling.blog.constant.RedisConst.COMMENT_USER_LIKE;

/**
 *
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
    implements CommentService{
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private RedisTemplate redisTemplate;
    @Resource
    private HttpServletRequest request;

    @Override
    @Transactional
    public void deleteBackCommentsByIdList(List<Integer> idList) {
        if (idList.isEmpty())
            throw new IllegalRequestException();
        int count = commentMapper.delete(new LambdaUpdateWrapper<Comment>()
                .eq(Comment::getDeletedFlag, true)
                .in(Comment::getId, idList));
        if (count != idList.size())
            throw new IllegalRequestException();
    }

    @Override
    @Transactional
    public void updateCommentsStatusBackVO(StatusBackVO statusBackVO) {
        LoginUser loginUser = UserUtil.getLoginUser();
        LambdaUpdateWrapper<Comment> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        if (RECYCLE.equals(statusBackVO.getType())) {
            if (loginUser.getRoleWeight() > 300)
                throw new OperationStatusException();
            if (statusBackVO.getStatus() == Boolean.TRUE)
                lambdaUpdateWrapper.set(Comment::getRecycleFlag, false).in(Comment::getId, statusBackVO.getIdList());
            else
                lambdaUpdateWrapper.set(Comment::getDeletedFlag, true).and(e -> e.in(Comment::getId, statusBackVO.getIdList()).or().in(Comment::getParentId, statusBackVO.getIdList()));
        } else if (DELETED.equals(statusBackVO.getType())) {
            if (loginUser.getRoleWeight() > 100)
                throw new IllegalRequestException();
            lambdaUpdateWrapper.set(Comment::getDeletedFlag, false).in(Comment::getId, statusBackVO.getIdList());
        } else
            lambdaUpdateWrapper.set(Comment::getRecycleFlag, true).and(e -> e.in(Comment::getId, statusBackVO.getIdList()).or().in(Comment::getParentId, statusBackVO.getIdList()));
        commentMapper.update(null, lambdaUpdateWrapper
                .eq(loginUser.getRoleWeight() > 100, Comment::getDeletedFlag, false)
                .ne(loginUser.getRoleWeight() > 200, Comment::getArticleId, -1)
                .and(loginUser.getRoleWeight() > 300, e -> e.eq(Comment::getUserId, loginUser.getUserId()).or().exists("select a.id from tb_article a where a.id = article_id and a.deleted_flag = false and a.user_id = " + loginUser.getUserId())
                .set(Comment::getUpdateUser, loginUser.getUserId())
                .set(Comment::getUpdateTime, new Date())));
    }

    @Override
    public PagePojo<CommentsBackDTO> getCommentsBackDTO(Condition condition) {
        LoginUser loginUser = UserUtil.getLoginUser();
        if ((DELETED.equals(condition.getType()) && loginUser.getRoleWeight() > 100) || (condition.getFlag() == Boolean.TRUE && loginUser.getRoleWeight() > 200))
            return new PagePojo<>();
        Integer count = commentMapper.selectCommentsBackDTOCount(condition, loginUser.getUserId(), loginUser.getRoleWeight());
        if (count == 0)
            return new PagePojo<>();
        condition.setCurrent((condition.getCurrent() - 1) * condition.getSize());
        List<CommentsBackDTO> commentsBackDTOList = commentMapper.selectCommentsBackDTO(condition, loginUser.getUserId(), loginUser.getRoleWeight());
        if (commentsBackDTOList.size() == 0)
            return new PagePojo<>(count, new ArrayList<>());
        Map<String, Integer> likeCountMap = redisTemplate.boundHashOps(COMMENT_LIKE_COUNT).entries();
        commentsBackDTOList.forEach(e -> e.setLikeCount(likeCountMap.get(e.getId().toString())));
        return new PagePojo<>(count, commentsBackDTOList);
    }

    @Override
    @Transactional
    public void saveCommentVO(CommentVO commentVO) {
        LoginUser loginUser =  UserUtil.getLoginUser();
        Integer loginUserId = loginUser.getUserId();
        Integer articleId = commentVO.getArticleId();
        Comment comment = new Comment();
        if (articleId != null && articleId != -1) {
            Integer count = articleMapper.selectCount(new LambdaQueryWrapper<Article>()
                    .eq(Article::getId, articleId)
                    .eq(Article::getDraftFlag, false)
                    .and(loginUser.getRoleWeight() > 300, e -> e
                            .and(e2 -> e2.eq(Article::getHiddenFlag, false).eq(Article::getCommentableFlag, true))
                            .or()
                            .eq(Article::getUserId, loginUserId)));
            if (count == 0)
                throw new OperationStatusException();
            comment.setArticleId(articleId);
            Integer parentId = commentVO.getParentId();
            if (parentId != null) {
                count = commentMapper.selectCount(new LambdaQueryWrapper<Comment>()
                        .eq(Comment::getId, parentId)
                        .eq(Comment::getArticleId, articleId)
                        .eq(Comment::getParentId, -1)
                        .eq(Comment::getRecycleFlag, false));
                if (count == 0)
                    throw new OperationStatusException();
                comment.setParentId(parentId);
                Integer replyId = commentVO.getReplyId();
                if (replyId != null) {
                    count = commentMapper.selectCount(new LambdaQueryWrapper<Comment>()
                            .eq(Comment::getUserId, replyId)
                            .eq(Comment::getArticleId, articleId)
                            .eq(Comment::getParentId, parentId)
                            .eq(Comment::getRecycleFlag, false));
                    if (count == 0)
                        throw new OperationStatusException();
                    comment.setReplyId(replyId);
                }
            }
        }
        comment.setUserId(loginUserId);
        comment.setCommentContent(RegexUtil.deleteHTMLTag(commentVO.getCommentContent()));
        comment.setIpAddress(IpUtil.getIpAddress(request));
        comment.setIpSource(comment.getIpAddress());
        comment.setCreateUser(loginUserId);
        comment.setCreateTime(new Date());
        commentMapper.insert(comment);
    }

    @Override
    @Transactional
    public void saveCommentLike(Integer id) {
        LoginUser loginUser = UserUtil.getLoginUser();
        Integer loginUserId = loginUser.getUserId();
        Integer count = commentMapper.selectCount(new LambdaQueryWrapper<Comment>()
                .eq(Comment::getId, id)
                .eq(Comment::getRecycleFlag, false)
                .and(loginUser.getRoleWeight() > 300, e -> e
                        .eq(Comment::getArticleId, -1)
                        .or()
                        .exists("select a.id from tb_article a where a.id=article_id and a.draft_flag=false and(a.hidden_flag=false or a.user_id="+loginUserId+")"))
                .exists(loginUser.getRoleWeight() > 300, ""));
        if (count == 0)
            throw new OperationStatusException();
        HashSet<Integer> commentLikeSet = (HashSet<Integer>) redisTemplate.boundHashOps(COMMENT_USER_LIKE).get(loginUserId.toString());
        if (commentLikeSet == null)
            commentLikeSet = new HashSet<>();
        if (commentLikeSet.contains(id)) {
            commentLikeSet.remove(id);
            redisTemplate.boundHashOps(COMMENT_LIKE_COUNT).increment(id.toString(), -1);
        } else {
            commentLikeSet.add(id);
            redisTemplate.boundHashOps(COMMENT_LIKE_COUNT).increment(id.toString(), 1);
        }
        redisTemplate.boundHashOps(COMMENT_USER_LIKE).put(loginUserId.toString(), commentLikeSet);
    }

    @Override
    public PagePojo<CommentsDTO> getCommentsDTO(Condition condition) {
        LoginUser loginUser = UserUtil.getLoginUser();
        Integer count = commentMapper.selectCommentsDTOCount(condition, loginUser.getUserId(), loginUser.getRoleWeight());
        if (count == 0)
            return new PagePojo<>();
        condition.setCurrent((condition.getCurrent() - 1) * condition.getSize());
        List<CommentsDTO> commentsDTOList = commentMapper.selectCommentsDTO(condition);
        if (commentsDTOList.size() == 0)
            return new PagePojo<>(count, new ArrayList<>());
        Map<String, Integer> likeCountMap = redisTemplate.boundHashOps(COMMENT_LIKE_COUNT).entries();
        List<Integer> commentsDTOIdList = new ArrayList<>();
        commentsDTOList.forEach(e -> {
            commentsDTOIdList.add(e.getId());
            e.setLikeCount(likeCountMap.get(e.getId().toString()));
        });
        List<CommentsReplyDTO> commentsReplyDTOList = commentMapper.selectCommentsReplyDTO(commentsDTOIdList);
        commentsReplyDTOList.forEach(e -> e.setLikeCount(likeCountMap.get(e.getId().toString())));
        Map<Integer, List<CommentsReplyDTO>> commentsReplyDTOMap = commentsReplyDTOList.stream().collect(Collectors.groupingBy(CommentsReplyDTO::getParentId));
        commentsDTOList.forEach(e -> {
            e.setCommentsReplyDTOList(commentsReplyDTOMap.get(e.getId()));
            List<CommentsReplyDTO> list = commentsReplyDTOMap.get(e.getId());
            e.setReplyCount(list == null ? 0 : list.size());
        });
        return new PagePojo<>(count, commentsDTOList);
    }

    @Override
    public List<CommentsReplyDTO> getCommentsReplyDTO(Condition condition) {
        LoginUser loginUser = UserUtil.getLoginUser();
        condition.setCurrent((condition.getCurrent() - 1) * condition.getSize());
        List<CommentsReplyDTO> commentsReplyDTOList = commentMapper.selectCommentsReplyDTOById(condition, loginUser.getUserId(), loginUser.getRoleWeight());
        Map<String, Integer> likeCountMap = redisTemplate.boundHashOps(COMMENT_LIKE_COUNT).entries();
        commentsReplyDTOList.forEach(e -> e.setLikeCount(likeCountMap.get(e.getId().toString())));
        return commentsReplyDTOList;
    }
}




