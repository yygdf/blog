package com.iksling.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iksling.blog.entity.Notice;
import com.iksling.blog.vo.StatusBackVO;

import java.util.List;
import java.util.Map;

/**
 *
 */
public interface NoticeService extends IService<Notice> {

    List<Map<String, Object>> getBackNotice(Integer type);

    void updateNoticesStatusBackVO(StatusBackVO statusBackVO);
}
