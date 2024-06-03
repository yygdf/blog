package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.entity.Notice;
import com.iksling.blog.exception.OperationStatusException;
import com.iksling.blog.mapper.NoticeMapper;
import com.iksling.blog.pojo.LoginUser;
import com.iksling.blog.service.NoticeService;
import com.iksling.blog.util.UserUtil;
import com.iksling.blog.vo.StatusBackVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 *
 */
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice>
    implements NoticeService{
    @Autowired
    private NoticeMapper noticeMapper;

    @Override
    public List<Map<String, Object>> getBackNotice(Integer type) {
        LoginUser loginUser = UserUtil.getLoginUser();
        switch (type) {
            case 2:
                return noticeMapper.selectBackReplyComments(loginUser.getUserId());
            case 3:
                return noticeMapper.selectBackReplyComments(loginUser.getUserId());
            case 4:
                return noticeMapper.selectBackReplyComments(loginUser.getUserId());
            case 5:
                return noticeMapper.selectBackReplyComments(loginUser.getUserId());
            default:
                return noticeMapper.selectBackReplyComments(loginUser.getUserId());
        }
    }

    @Override
    @Transactional
    public void updateNoticesStatusBackVO(StatusBackVO statusBackVO) {
        LoginUser loginUser = UserUtil.getLoginUser();
        int count = noticeMapper.update(null, new LambdaUpdateWrapper<Notice>()
                .and(loginUser.getRoleWeight() > 200, e -> e.eq(Notice::getUserId, loginUser.getUserId()).eq(Notice::getDeletedFlag, false))
                .in(Notice::getId, statusBackVO.getIdList())
                .set(Notice::getDeletedFlag, true));
        if (count != statusBackVO.getIdList().size())
            throw new OperationStatusException();
    }
}




