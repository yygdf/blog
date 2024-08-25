package com.iksling.blog.controller;

import com.iksling.blog.annotation.OptLog;
import com.iksling.blog.pojo.Result;
import com.iksling.blog.service.FriendLinkService;
import com.iksling.blog.pojo.Condition;
import com.iksling.blog.vo.FriendLinkBackVO;
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
@Api(tags = "友链模块")
public class FriendLinkController {
    @Autowired
    private FriendLinkService friendLinkService;

    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "添加或修改友链")
    @ApiImplicitParam(name = "friendLinkBackVO", value = "友链后台VO", required = true, dataType = "FriendLinkBackVO")
    @PostMapping("/back/friendLink")
    public Result saveOrUpdateBackFriendLink(@Valid @RequestBody FriendLinkBackVO friendLinkBackVO) {
        friendLinkService.saveOrUpdateFriendLinkBackVO(friendLinkBackVO);
        return Result.success().message("操作成功");
    }

    @OptLog(optType = REMOVE)
    @ApiOperation(value = "物理批量删除友链")
    @ApiImplicitParam(name = "idList", value = "idList", required = true, dataType = "List<Integer>")
    @DeleteMapping("/back/friendLinks")
    public Result deleteBackFriendLinks(@RequestBody List<Integer> idList) {
        friendLinkService.deleteBackFriendLinksByIdList(idList);
        return Result.success().message("操作成功");
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "批量更新友链状态")
    @ApiImplicitParam(name = "statusBackVO", value = "状态后台VO", required = true, dataType = "StatusBackVO")
    @PutMapping("/back/friendLinks/status")
    public Result updateFriendLinksStatus(@Valid @RequestBody StatusBackVO statusBackVO) {
        friendLinkService.updateFriendLinksStatusBackVO(statusBackVO);
        return Result.success().message("操作成功");
    }

    @ApiOperation(value = "查看后台友链列表")
    @ApiImplicitParam(name = "condition", value = "查询条件", required = true, dataType = "Condition")
    @GetMapping("/back/friendLinks")
    public Result getBackFriendLinks(@Valid Condition condition) {
        return Result.success().message("查询成功").data(friendLinkService.getFriendLinksBackDTO(condition));
    }

    /****************************************************************************************************/

    @ApiOperation(value = "查看友链列表")
    @GetMapping("/friendLinks")
    public Result getFriendLinks() {
        return Result.success().message("查询成功").data(friendLinkService.getFriendLinksDTO());
    }
}
