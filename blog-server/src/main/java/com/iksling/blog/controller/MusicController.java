package com.iksling.blog.controller;

import com.iksling.blog.annotation.OptLog;
import com.iksling.blog.pojo.Condition;
import com.iksling.blog.pojo.Result;
import com.iksling.blog.service.MusicService;
import com.iksling.blog.util.LocaleUtil;
import com.iksling.blog.vo.MusicBackVO;
import com.iksling.blog.vo.StatusBackVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.iksling.blog.constant.LogConst.*;

@RestController
@Api(tags = "音乐模块")
public class MusicController {
    @Autowired
    private MusicService musicService;

    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "添加或修改音乐")
    @ApiImplicitParam(name = "musicBackVO", value = "音乐后台VO", required = true, dataType = "FriendLinkBackVO")
    @PostMapping("/back/music")
    public Result saveOrUpdateBackMusic(@Valid @RequestBody MusicBackVO musicBackVO) {
        musicService.saveOrUpdateMusicBackVO(musicBackVO);
        return Result.success().message(LocaleUtil.getMessage("C0001"));
    }

    @OptLog(optType = REMOVE)
    @ApiOperation(value = "物理批量删除音乐")
    @ApiImplicitParam(name = "idList", value = "idList", required = true, dataType = "List<Integer>")
    @DeleteMapping("/back/musics")
    public Result deleteBackMusics(@RequestBody List<Integer> idList) {
        musicService.deleteBackMusicsByIdList(idList);
        return Result.success().message(LocaleUtil.getMessage("C0001"));
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "批量更新音乐状态")
    @ApiImplicitParam(name = "statusBackVO", value = "状态后台VO", required = true, dataType = "StatusBackVO")
    @PutMapping("/back/musics/status")
    public Result updateMusicsStatus(@Valid @RequestBody StatusBackVO statusBackVO) {
        musicService.updateMusicsStatusBackVO(statusBackVO);
        return Result.success().message(LocaleUtil.getMessage("C0001"));
    }

    @ApiOperation(value = "查看后台音乐列表")
    @ApiImplicitParam(name = "condition", value = "查询条件", required = true, dataType = "Condition")
    @GetMapping("/back/musics")
    public Result getBackMusics(@Valid Condition condition) {
        return Result.success().message(LocaleUtil.getMessage("C0003")).data(musicService.getMusicsBackDTO(condition));
    }

    /****************************************************************************************************/

    @OptLog(optType = SAVE)
    @ApiOperation(value = "收藏音乐")
    @ApiImplicitParam(name = "id", value = "音乐id", required = true, dataType = "Integer")
    @PostMapping("/music/collect/{id}")
    public Result saveMusicCollect(@PathVariable Integer id) {
        musicService.saveMusicCollect(id);
        return Result.success().message(LocaleUtil.getMessage("C0001"));
    }

    @ApiOperation(value = "查看音乐列表")
    @ApiImplicitParam(name = "condition", value = "查询条件", required = true, dataType = "Condition")
    @GetMapping("/musics")
    public Result getMusics(@Valid Condition condition) {
        return Result.success().message(LocaleUtil.getMessage("C0003")).data(musicService.getMusicsDTO(condition));
    }

    @ApiOperation(value = "查看收藏音乐列表")
    @ApiImplicitParam(name = "condition", value = "查询条件", required = true, dataType = "Condition")
    @GetMapping("/musics/collection")
    public Result getMusicsCollection(@Valid Condition condition) {
        return Result.success().message(LocaleUtil.getMessage("C0003")).data(musicService.getMusicsCollectionDTO(condition));
    }
}
