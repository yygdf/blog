package com.iksling.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iksling.blog.entity.Notice;
import com.iksling.blog.pojo.Condition;
import com.iksling.blog.vo.StatusBackVO;

import java.util.List;
import java.util.Map;

/**
 *
 */
public interface NoticeService extends IService<Notice> {

    void updateNoticesStatusBackVO(StatusBackVO statusBackVO);

    void updateNoticesStatusRead(StatusBackVO statusBackVO);

    List<Map<String, Object>> getBackNotices(Condition condition);

    Map<Integer, List<Integer>> getBackNoticesUnread();
}
