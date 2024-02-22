package com.iksling.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iksling.blog.dto.OperationLogsBackDTO;
import com.iksling.blog.entity.OperationLog;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.vo.ConditionBackVO;

import java.util.HashMap;

/**
 *
 */
public interface BlogService {
    HashMap<String, Object> getBlogInfo(Integer bloggerId);
}
