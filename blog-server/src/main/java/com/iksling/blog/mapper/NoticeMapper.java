package com.iksling.blog.mapper;

import com.iksling.blog.entity.Notice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iksling.blog.pojo.Condition;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Entity com.iksling.blog.entity.Notice
 */
public interface NoticeMapper extends BaseMapper<Notice> {

    @MapKey("id")
    List<Map<String, Object>> selectBackReplyComments(@Param("condition") Condition condition);

    @MapKey("id")
    List<Map<String, Object>> selectBackLikes(@Param("condition") Condition condition);

    @MapKey("id")
    List<Map<String, Object>> selectBackSystemNotices(@Param("condition") Condition condition);
}




