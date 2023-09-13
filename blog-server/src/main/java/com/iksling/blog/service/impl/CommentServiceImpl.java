package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.dto.CommentsBackDTO;
import com.iksling.blog.entity.Comment;
import com.iksling.blog.exception.IllegalRequestException;
import com.iksling.blog.mapper.CommentMapper;
import com.iksling.blog.pojo.LoginUser;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.service.CommentService;
import com.iksling.blog.util.UserUtil;
import com.iksling.blog.vo.ConditionVO;
import com.iksling.blog.vo.UpdateBatchVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    public PagePojo<CommentsBackDTO> getPageCommentsBackDTO(ConditionVO condition) {
        LoginUser loginUser = UserUtil.getLoginUser();
        if (loginUser.getRoleWeight() > 100 && Objects.equals(condition.getDeletedFlag(), true))
            throw new IllegalRequestException();
        condition.setCurrent((condition.getCurrent() - 1) * condition.getSize());
        List<CommentsBackDTO> commentsBackDTOList = commentMapper.listCommentsBackDTO(condition, loginUser.getUserId(), loginUser.getRoleWeight());
        if (CollectionUtils.isEmpty(commentsBackDTOList))
            return new PagePojo<>();
        Map<String, Integer> likeCountMap = redisTemplate.boundHashOps(COMMENT_LIKE_COUNT).entries();
        commentsBackDTOList.forEach(item -> {
            item.setLikeCount(Objects.requireNonNull(likeCountMap).get(item.getId().toString()));
        });
        return new PagePojo<>(commentsBackDTOList.size(), commentsBackDTOList);
    }

    @Override
    @Transactional
    public void updateCommentsStatus(UpdateBatchVO updateBatchVO) {
        LoginUser loginUser = UserUtil.getLoginUser();
        commentMapper.updateCommentsStatus(updateBatchVO, loginUser.getUserId(), loginUser.getRoleWeight());
    }

    @Override
    @Transactional
    public void deleteCommentIdList(List<Integer> commentIdList) {
        if (CollectionUtils.isEmpty(commentIdList))
            throw new IllegalRequestException();
        int count = commentMapper.deleteBatchIds(commentIdList);
        if (count != commentIdList.size())
            throw new IllegalRequestException();
    }
}




