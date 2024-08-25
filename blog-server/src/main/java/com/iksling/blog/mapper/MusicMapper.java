package com.iksling.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iksling.blog.dto.MusicsBackDTO;
import com.iksling.blog.entity.Music;
import com.iksling.blog.pojo.Condition;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Entity com.iksling.blog.entity.Music
 */
public interface MusicMapper extends BaseMapper<Music> {

    Integer selectMusicsBackDTOCount(@Param("condition") Condition condition, Integer userId, Integer roleWeight);

    List<MusicsBackDTO> selectMusicsBackDTO(@Param("condition") Condition condition, Integer userId, Integer roleWeight);
}




