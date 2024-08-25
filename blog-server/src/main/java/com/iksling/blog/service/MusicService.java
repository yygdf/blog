package com.iksling.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iksling.blog.dto.MusicsBackDTO;
import com.iksling.blog.dto.MusicsDTO;
import com.iksling.blog.entity.Music;
import com.iksling.blog.pojo.Condition;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.vo.MusicBackVO;
import com.iksling.blog.vo.StatusBackVO;

import java.util.List;

/**
 *
 */
public interface MusicService extends IService<Music> {

    void saveOrUpdateMusicBackVO(MusicBackVO musicBackVO);

    void deleteBackMusicsByIdList(List<Integer> idList);

    void updateMusicsStatusBackVO(StatusBackVO statusBackVO);

    PagePojo<MusicsBackDTO> getMusicsBackDTO(Condition condition);

    void saveMusicCollect(Integer id);

    List<MusicsDTO> getMusicsDTO(Condition condition);

    List<MusicsDTO> getMusicsCollectionDTO(Condition condition);
}
