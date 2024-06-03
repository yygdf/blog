package com.iksling.blog.mapper;

import com.iksling.blog.entity.Notice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.MapKey;

import java.util.List;
import java.util.Map;

/**
 * @Entity com.iksling.blog.entity.Notice
 */
public interface NoticeMapper extends BaseMapper<Notice> {

    @MapKey("id")
    List<Map<String, Object>> selectBackReplyComments(Integer userId);
}




