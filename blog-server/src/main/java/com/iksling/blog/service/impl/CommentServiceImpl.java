package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.dto.CommentsBackDTO;
import com.iksling.blog.entity.Comment;
import com.iksling.blog.exception.IllegalRequestException;
import com.iksling.blog.exception.OperationStatusException;
import com.iksling.blog.mapper.CommentMapper;
import com.iksling.blog.pojo.LoginUser;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.service.CommentService;
import com.iksling.blog.util.UserUtil;
import com.iksling.blog.vo.ConditionBackVO;
import com.iksling.blog.vo.StatusBackVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.iksling.blog.constant.FlagConst.DELETED;
import static com.iksling.blog.constant.FlagConst.RECYCLE;
import static com.iksling.blog.constant.RedisConst.COMMENT_LIKE_COUNT;

/**
 *
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
    implements CommentService{
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private RedisTemplate redisTemplate;

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
    public PagePojo<CommentsBackDTO> getCommentsBackDTO(ConditionBackVO condition) {
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
}




