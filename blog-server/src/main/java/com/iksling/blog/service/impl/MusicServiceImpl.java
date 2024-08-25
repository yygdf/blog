package com.iksling.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.dto.MusicsBackDTO;
import com.iksling.blog.dto.MusicsDTO;
import com.iksling.blog.entity.Music;
import com.iksling.blog.exception.IllegalRequestException;
import com.iksling.blog.exception.OperationStatusException;
import com.iksling.blog.mapper.MusicMapper;
import com.iksling.blog.pojo.Condition;
import com.iksling.blog.pojo.LoginUser;
import com.iksling.blog.pojo.PagePojo;
import com.iksling.blog.service.MusicService;
import com.iksling.blog.util.BeanCopyUtil;
import com.iksling.blog.util.RedisUtil;
import com.iksling.blog.util.UserUtil;
import com.iksling.blog.vo.MusicBackVO;
import com.iksling.blog.vo.StatusBackVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import static com.iksling.blog.constant.FlagConst.DELETED;
import static com.iksling.blog.constant.RedisConst.MUSIC_USER_COLLECT;

/**
 *
 */
@Service
public class MusicServiceImpl extends ServiceImpl<MusicMapper, Music>
    implements MusicService{
    @Autowired
    private MusicMapper musicMapper;

    @Resource
    private HttpServletRequest request;

    @Override
    @Transactional
    public void saveOrUpdateMusicBackVO(MusicBackVO musicBackVO) {
        LoginUser loginUser = UserUtil.getLoginUser();
        Integer loginUserId = loginUser.getUserId();
        Music music = BeanCopyUtil.copyObject(musicBackVO, Music.class);
        if (music.getId() == null) {
            if (music.getMusicUrl() == null || music.getMusicName() == null)
                throw new OperationStatusException();
            music.setUserId(loginUserId);
            music.setCreateUser(loginUserId);
            music.setCreateTime(new Date());
            musicMapper.insert(music);
        } else {
            Music musicOrigin = musicMapper.selectOne(new LambdaQueryWrapper<Music>()
                    .select(Music::getUserId)
                    .eq(Music::getDeletedFlag, false)
                    .eq(Music::getId, music.getId())
                    .eq(loginUser.getRoleWeight() > 200, Music::getUserId, loginUserId));
            if (musicOrigin == null)
                throw new OperationStatusException();
            music.setUpdateUser(loginUserId);
            music.setUpdateTime(new Date());
            musicMapper.updateById(music);
        }
    }

    @Override
    @Transactional
    public void deleteBackMusicsByIdList(List<Integer> idList) {
        if (idList.isEmpty())
            throw new IllegalRequestException();
        int count = musicMapper.delete(new LambdaUpdateWrapper<Music>()
                .eq(Music::getDeletedFlag, true)
                .in(Music::getId, idList));
        if (count != idList.size())
            throw new IllegalRequestException();
    }

    @Override
    @Transactional
    public void updateMusicsStatusBackVO(StatusBackVO statusBackVO) {
        LoginUser loginUser = UserUtil.getLoginUser();
        LambdaUpdateWrapper<Music> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        if (DELETED.equals(statusBackVO.getType())) {
            if (loginUser.getRoleWeight() > 100)
                throw new IllegalRequestException();
            lambdaUpdateWrapper.set(Music::getDeletedFlag, false);
        } else
            lambdaUpdateWrapper.set(Music::getDeletedFlag, true);
        int count = musicMapper.update(null, lambdaUpdateWrapper
                .eq(loginUser.getRoleWeight() > 100, Music::getDeletedFlag, false)
                .eq(loginUser.getRoleWeight() > 200, Music::getUserId, loginUser.getUserId())
                .in(Music::getId, statusBackVO.getIdList())
                .set(Music::getUpdateUser, loginUser.getUserId())
                .set(Music::getUpdateTime, new Date()));
        if (count != statusBackVO.getIdList().size())
            throw new OperationStatusException();
    }

    @Override
    public PagePojo<MusicsBackDTO> getMusicsBackDTO(Condition condition) {
        LoginUser loginUser = UserUtil.getLoginUser();
        if (DELETED.equals(condition.getType()) && loginUser.getRoleWeight() > 100)
            return new PagePojo<>();
        Integer count = musicMapper.selectMusicsBackDTOCount(condition, loginUser.getUserId(), loginUser.getRoleWeight());
        if (count == 0)
            return new PagePojo<>();
        condition.setCurrent((condition.getCurrent() - 1) * condition.getSize());
        List<MusicsBackDTO> musicsBackDTOList = musicMapper.selectMusicsBackDTO(condition, loginUser.getUserId(), loginUser.getRoleWeight());
        return new PagePojo<>(count, musicsBackDTOList);
    }

    @Override
    @Transactional
    public void saveMusicCollect(Integer id) {
        LoginUser loginUser = UserUtil.getLoginUser();
        Integer loginUserId = loginUser.getUserId();
        List<Object> objectList = musicMapper.selectObjs(new LambdaQueryWrapper<Music>()
                .select(Music::getUserId)
                .eq(Music::getId, id)
                .eq(Music::getDeletedFlag, false));
        if (objectList.size() == 0)
            throw new OperationStatusException();
        HashSet<Integer> musicCollectSet = RedisUtil.getMapValue(MUSIC_USER_COLLECT, loginUserId.toString());
        if (musicCollectSet == null)
            musicCollectSet = new HashSet<>();
        if (musicCollectSet.contains(id))
            musicCollectSet.remove(id);
        else
            musicCollectSet.add(id);
        RedisUtil.setMapValue(MUSIC_USER_COLLECT, loginUserId.toString(), musicCollectSet);
    }

    @Override
    public List<MusicsDTO> getMusicsDTO(Condition condition) {
        Page<Music> musicPage = new Page<>(condition.getCurrent(), condition.getSize());
        return BeanCopyUtil.copyList(musicMapper.selectPage(musicPage, new LambdaQueryWrapper<Music>()
                .select(Music::getId, Music::getUserId, Music::getMusicUrl, Music::getMusicName, Music::getMusicCover, Music::getMusicWords, Music::getAuthor, Music::getAlbum)
                .eq(Music::getUserId, Integer.valueOf(request.getHeader("Blogger-Id")))
                .eq(Music::getDeletedFlag, false)
                .orderByDesc(Music::getId)).getRecords(), MusicsDTO.class);
    }

    @Override
    public List<MusicsDTO> getMusicsCollectionDTO(Condition condition) {
        LoginUser loginUser = UserUtil.getLoginUser();
        Integer loginUserId = loginUser.getUserId();
        HashSet<Integer> musicCollectSet = RedisUtil.getMapValue(MUSIC_USER_COLLECT, loginUserId.toString());
        if (musicCollectSet == null || musicCollectSet.isEmpty())
            return new ArrayList<>();
        Page<Music> musicPage = new Page<>(condition.getCurrent(), condition.getSize());
        return BeanCopyUtil.copyList(musicMapper.selectPage(musicPage, new LambdaQueryWrapper<Music>()
                .select(Music::getId, Music::getUserId, Music::getMusicUrl, Music::getMusicName, Music::getMusicCover, Music::getMusicWords, Music::getAuthor, Music::getAlbum)
                .in(Music::getId, musicCollectSet)
                .eq(Music::getDeletedFlag, false)
                .orderByDesc(Music::getId)).getRecords(), MusicsDTO.class);
    }
}




