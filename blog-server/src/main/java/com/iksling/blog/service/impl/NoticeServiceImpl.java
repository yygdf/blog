package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.entity.Notice;
import com.iksling.blog.entity.User;
import com.iksling.blog.exception.OperationStatusException;
import com.iksling.blog.mapper.NoticeMapper;
import com.iksling.blog.mapper.UserMapper;
import com.iksling.blog.pojo.Condition;
import com.iksling.blog.pojo.LoginUser;
import com.iksling.blog.service.NoticeService;
import com.iksling.blog.util.EmailUtil;
import com.iksling.blog.util.UserUtil;
import com.iksling.blog.vo.NoticeBackVO;
import com.iksling.blog.vo.StatusBackVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.iksling.blog.constant.CommonConst.USER_MESSAGE_CONFIG_MAP;
import static com.iksling.blog.constant.RedisConst.USER_MESSAGE_CONFIG;

/**
 *
 */
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice>
    implements NoticeService{
    @Autowired
    private NoticeMapper noticeMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @PostConstruct
    public void loadMessageConfigMap() {
        USER_MESSAGE_CONFIG_MAP = new HashMap<>(redisTemplate.opsForHash().entries(USER_MESSAGE_CONFIG));
    }

    @Override
    @Transactional
    public void saveNoticeBackVO(NoticeBackVO noticeBackVO) {
        Integer loginUserId = UserUtil.getLoginUser().getUserId();
        Integer userId = noticeBackVO.getUserId();
        if (noticeBackVO.getType() == 2) {
            List<Object> objectList = userMapper.selectObjs(new LambdaQueryWrapper<User>()
                    .select(User::getEmail)
                    .eq(User::getId, userId));
            String email = objectList.get(0).toString();
            if (!email.equals(""))
                EmailUtil.sendEmail(email, noticeBackVO.getNoticeTitle(), noticeBackVO.getNoticeContent());
        } else {
            noticeMapper.insert(Notice.builder()
                    .userId(userId)
                    .noticeType(4)
                    .noticeTypeSub(9)
                    .noticeTitle(noticeBackVO.getNoticeTitle())
                    .noticeContent(noticeBackVO.getNoticeContent())
                    .createUser(loginUserId)
                    .createTime(new Date()).build());
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

    @Override
    @Transactional
    public void updateNoticesStatusRead(StatusBackVO statusBackVO) {
        LoginUser loginUser = UserUtil.getLoginUser();
        int count = noticeMapper.update(null, new LambdaUpdateWrapper<Notice>()
                .and(loginUser.getRoleWeight() > 200, e -> e.eq(Notice::getUserId, loginUser.getUserId()).eq(Notice::getDeletedFlag, false).eq(Notice::getReadFlag, false))
                .in(Notice::getId, statusBackVO.getIdList())
                .set(Notice::getReadFlag, true));
        if (count != statusBackVO.getIdList().size())
            throw new OperationStatusException();
    }

    @Override
    public List<Map<String, Object>> getBackNotices(Condition condition) {
        condition.setUserId(UserUtil.getLoginUser().getUserId());
        condition.setCurrent((condition.getCurrent() - 1) * condition.getSize());
        switch (condition.getType()) {
            case 3:
                return noticeMapper.selectBackLikes(condition);
            case 4:
                return noticeMapper.selectBackSystemNotices(condition);
            default:
                return noticeMapper.selectBackReplyComments(condition);
        }
    }

    @Override
    public Map<Integer, List<Integer>> getBackNoticesUnread() {
        List<Notice> noticeList = noticeMapper.selectList(new LambdaQueryWrapper<Notice>()
                .select(Notice::getId, Notice::getNoticeType)
                .eq(Notice::getDeletedFlag, false)
                .eq(Notice::getReadFlag, false)
                .eq(Notice::getUserId, UserUtil.getLoginUser().getUserId()));
        return noticeList.stream().collect(Collectors.groupingBy(Notice::getNoticeType, Collectors.mapping(Notice::getId, Collectors.toList())));
    }
}




