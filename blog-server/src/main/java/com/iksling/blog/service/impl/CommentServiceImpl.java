package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.dto.CommentsBackDTO;
import com.iksling.blog.dto.CommentsDTO;
import com.iksling.blog.dto.CommentsReplyDTO;
import com.iksling.blog.entity.Article;
import com.iksling.blog.entity.Comment;
import com.iksling.blog.entity.Notice;
import com.iksling.blog.exception.IllegalRequestException;
import com.iksling.blog.exception.OperationStatusException;
import com.iksling.blog.mapper.ArticleMapper;
import com.iksling.blog.mapper.CommentMapper;
import com.iksling.blog.mapper.NoticeMapper;
import com.iksling.blog.pojo.Condition;
import com.iksling.blog.pojo.LoginUser;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.service.CommentService;
import com.iksling.blog.util.*;
import com.iksling.blog.vo.CommentVO;
import com.iksling.blog.vo.StatusBackVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

import static com.iksling.blog.constant.CommonConst.ROOT_USER_ID;
import static com.iksling.blog.constant.CommonConst.WEBSITE_URL;
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
    private NoticeMapper noticeMapper;

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
        if (commentsBackDTOList.isEmpty())
            return new PagePojo<>(count, new ArrayList<>());
        Map<String, Integer> likeCountMap = RedisUtil.getMap(COMMENT_LIKE_COUNT);
        commentsBackDTOList.forEach(e -> e.setLikeCount(likeCountMap.get(e.getId().toString())));
        return new PagePojo<>(count, commentsBackDTOList);
    }

    @Override
    @Transactional
    public void saveCommentVO(CommentVO commentVO) {
        LoginUser loginUser =  UserUtil.getLoginUser();
        Integer loginUserId = loginUser.getUserId();
        Integer replyId = commentVO.getReplyId();
        Integer articleId = commentVO.getArticleId() == null ? -1 : commentVO.getArticleId();
        Integer parentId = commentVO.getParentId();
        Comment comment = new Comment();
        Notice notice = new Notice();
        boolean sendNoticeFlag = true;
        if (articleId == -1) {
            if (parentId == null) {
                notice.setUserId(ROOT_USER_ID);
                notice.setNoticeType(4);
                notice.setNoticeTypeSub(1);
                notice.setNoticeTitle(LocaleUtil.getMessage("S0035"));
                notice.setNoticeContent(LocaleUtil.getMessage("S0036", loginUser.getUsername(), WEBSITE_URL, ROOT_USER_ID));
            } else {
                if (!loginUserId.equals(ROOT_USER_ID))
                    throw new OperationStatusException();
                comment.setParentId(parentId);
                sendNoticeFlag = false;
            }
        } else {
            List<Object> objectList = articleMapper.selectObjs(new LambdaQueryWrapper<Article>()
                    .select(Article::getUserId)
                    .eq(Article::getId, articleId)
                    .eq(Article::getDraftFlag, false)
                    .and(loginUser.getRoleWeight() > 300, e -> e
                            .and(e2 -> e2.eq(Article::getHiddenFlag, false).eq(Article::getCommentableFlag, true))
                            .or()
                            .eq(Article::getUserId, loginUserId)));
            if (objectList.size() == 0)
                throw new OperationStatusException();
            comment.setArticleId(articleId);
            Integer userId = (Integer) objectList.get(0);
            HashMap<String, Integer> map = UserUtil.getUserMessageConfig(userId);
            if (loginUserId.equals(userId) || map.get("1") == 2)
                sendNoticeFlag = false;
            else {
                notice.setUserId(userId);
                notice.setArticleId(articleId);
                notice.setNoticeType(1);
            }
            if (parentId != null) {
                Integer count = commentMapper.selectCount(new LambdaQueryWrapper<Comment>()
                        .eq(Comment::getId, parentId)
                        .eq(Comment::getArticleId, articleId)
                        .eq(Comment::getParentId, -1)
                        .eq(Comment::getRecycleFlag, false));
                if (count == 0)
                    throw new OperationStatusException(LocaleUtil.getMessage("S0009"));
                comment.setParentId(parentId);
            }
            if (replyId != null) {
                Integer count = commentMapper.selectCount(new LambdaQueryWrapper<Comment>()
                        .eq(Comment::getParentId, parentId)
                        .eq(Comment::getUserId, replyId)
                        .eq(Comment::getRecycleFlag, false));
                if (count == 0)
                    throw new OperationStatusException(LocaleUtil.getMessage("S0009"));
                comment.setReplyId(replyId);
                if (map.get("2") == 2)
                    sendNoticeFlag = false;
                else
                    notice.setNoticeType(2);
            }
        }
        Date createTime = new Date();
        comment.setUserId(loginUserId);
        comment.setCommentContent(RegexUtil.deleteHTMLTag(commentVO.getCommentContent()));
        comment.setIpAddress(IpUtil.getIpAddress(request));
        comment.setIpSource(comment.getIpAddress());
        comment.setCreateUser(loginUserId);
        comment.setCreateTime(createTime);
        commentMapper.insert(comment);
        if (sendNoticeFlag) {
            notice.setCommentId(comment.getId());
            notice.setCreateUser(loginUserId);
            notice.setCreateTime(createTime);
            noticeMapper.insert(notice);
        }
    }

    @Override
    @Transactional
    public void saveCommentLike(Integer id) {
        LoginUser loginUser = UserUtil.getLoginUser();
        Integer loginUserId = loginUser.getUserId();
        List<Map<String, Object>> mapList = commentMapper.selectMaps(new LambdaQueryWrapper<Comment>()
                .select(Comment::getUserId, Comment::getArticleId)
                .eq(Comment::getId, id)
                .eq(Comment::getRecycleFlag, false));
        if (mapList.size() == 0)
            throw new OperationStatusException(LocaleUtil.getMessage("S0010"));
        Integer articleId = (Integer) mapList.get(0).get("article_id");
        if (articleId != -1) {
            int count = articleMapper.selectCount(new LambdaQueryWrapper<Article>()
                    .eq(Article::getId, articleId)
                    .eq(Article::getDraftFlag, false)
                    .and(loginUser.getRoleWeight() > 300, e -> e.and(e2 -> e2.eq(Article::getHiddenFlag, false).eq(Article::getCommentableFlag, true)).or().eq(Article::getUserId, loginUser.getUserId())));
            if (count == 0)
                throw new OperationStatusException(LocaleUtil.getMessage("S0011"));
        }
        HashSet<Integer> commentLikeSet = RedisUtil.getMapValue(COMMENT_USER_LIKE, loginUserId.toString());
        if (commentLikeSet == null)
            commentLikeSet = new HashSet<>();
        if (commentLikeSet.contains(id)) {
            commentLikeSet.remove(id);
            RedisUtil.increment(COMMENT_LIKE_COUNT, id.toString(), -1);
        } else {
            commentLikeSet.add(id);
            RedisUtil.increment(COMMENT_LIKE_COUNT, id.toString(), 1);
            Integer userId = (Integer) mapList.get(0).get("user_id");
            HashMap<String, Integer> map = UserUtil.getUserMessageConfig(userId);
            if (!loginUserId.equals(userId) && map.get("3") == 1)
                noticeMapper.insert(Notice.builder()
                        .userId(userId)
                        .articleId(articleId)
                        .commentId(id)
                        .noticeType(3)
                        .noticeTypeSub(articleId == -1 ? 2 : 3)
                        .createUser(loginUserId)
                        .createTime(new Date()).build());
        }
        RedisUtil.setMapValue(COMMENT_USER_LIKE, loginUserId.toString(), commentLikeSet);
    }

    @Override
    public PagePojo<CommentsDTO> getCommentsDTO(Condition condition) {
        LoginUser loginUser = UserUtil.getLoginUser();
        Integer count = commentMapper.selectCommentsDTOCount(condition, loginUser.getUserId(), loginUser.getRoleWeight());
        if (count == 0)
            return new PagePojo<>();
        condition.setCurrent((condition.getCurrent() - 1) * condition.getSize());
        List<CommentsDTO> commentsDTOList = commentMapper.selectCommentsDTO(condition);
        if (commentsDTOList.isEmpty())
            return new PagePojo<>(count, new ArrayList<>());
        Map<String, Integer> likeCountMap = RedisUtil.getMap(COMMENT_LIKE_COUNT);
        List<Integer> commentsDTOIdList = new ArrayList<>();
        commentsDTOList.forEach(e -> {
            commentsDTOIdList.add(e.getId());
            e.setLikeCount(likeCountMap.get(e.getId().toString()));
        });
        List<Map<String, Object>> mapList = commentMapper.selectMaps(new QueryWrapper<Comment>()
                .select("parent_id,count(0) count")
                .eq("recycle_flag", false)
                .in("parent_id", commentsDTOIdList)
                .groupBy("parent_id"));
        Map<Integer, Long> map = mapList.stream().collect(Collectors.toMap(e -> (Integer) e.get("parent_id"), e -> (Long) e.get("count"), (key1, key2) -> key2, HashMap::new));
        List<CommentsReplyDTO> commentsReplyDTOList = commentMapper.selectCommentsReplyDTO(commentsDTOIdList);
        commentsReplyDTOList.forEach(e -> e.setLikeCount(likeCountMap.get(e.getId().toString())));
        Map<Integer, List<CommentsReplyDTO>> commentsReplyDTOMap = commentsReplyDTOList.stream().collect(Collectors.groupingBy(CommentsReplyDTO::getParentId));
        commentsDTOList.forEach(e -> {
            e.setCommentsReplyDTOList(commentsReplyDTOMap.get(e.getId()));
            Long replyCount = map.get(e.getId());
            e.setReplyCount(replyCount == null ? 0 : replyCount.intValue());
        });
        return new PagePojo<>(count, commentsDTOList);
    }

    @Override
    public List<CommentsReplyDTO> getCommentsReplyDTO(Condition condition) {
        LoginUser loginUser = UserUtil.getLoginUser();
        condition.setCurrent((condition.getCurrent() - 1) * condition.getSize());
        List<CommentsReplyDTO> commentsReplyDTOList = commentMapper.selectCommentsReplyDTOById(condition, loginUser.getUserId(), loginUser.getRoleWeight());
        Map<String, Integer> likeCountMap = RedisUtil.getMap(COMMENT_LIKE_COUNT);
        commentsReplyDTOList.forEach(e -> e.setLikeCount(likeCountMap.get(e.getId().toString())));
        return commentsReplyDTOList;
    }
}




