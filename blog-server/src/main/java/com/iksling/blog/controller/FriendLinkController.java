package com.iksling.blog.controller;

import com.iksling.blog.annotation.OptLog;
import com.iksling.blog.pojo.Result;
import com.iksling.blog.service.FriendLinkService;
import com.iksling.blog.vo.ConditionVO;
import com.iksling.blog.vo.FriendLinkBackVO;
import com.iksling.blog.vo.UpdateBatchVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.iksling.blog.constant.OptLogConst.*;

@RestController
@Api(tags = "友链模块")
public class FriendLinkController {
    @Autowired
    private FriendLinkService friendLinkService;

    @ApiOperation(value = "查看后台友链列表")
    @ApiImplicitParam(name = "condition", value = "查询条件", required = true, dataType = "ConditionVO")
    @GetMapping("/back/friendLinks")
    public Result listBackFriendLinks(@Valid ConditionVO condition) {
        return Result.success().message("查询成功").data(friendLinkService.getPageFriendLinksBackDTO(condition));
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "批量更新友链状态")
    @ApiImplicitParam(name = "updateBatchVO", value = "批量更新VO", required = true, dataType = "UpdateBatchVO")
    @PutMapping("/back/friendLinks")
    public Result updateFriendLinksStatus(@Valid UpdateBatchVO updateBatchVO) {
        friendLinkService.updateFriendLinksStatus(updateBatchVO);
        return Result.success().message("操作成功");
    }

    @OptLog(optType = REMOVE)
    @ApiOperation(value = "批量删除友链")
    @ApiImplicitParam(name = "friendLinkIdList", value = "友链idList", required = true, dataType = "List<Integer>")
    @DeleteMapping("/back/friendLinks")
    public Result deleteBackFriendLinks(@RequestBody List<Integer> friendLinkIdList) {
        friendLinkService.deleteFriendLinkIdList(friendLinkIdList);
        return Result.success().message("操作成功");
    }

    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "添加或修改友链")
    @ApiImplicitParam(name = "friendLinkBackVO", value = "友链后台VO", required = true, dataType = "FriendLinkBackVO")
    @PostMapping("/back/friendLink")
    public Result saveOrUpdateBackFriendLink(@Valid @RequestBody FriendLinkBackVO friendLinkBackVO) {
        friendLinkService.saveOrUpdateFriendLinkBackVO(friendLinkBackVO);
        return Result.success().message("操作成功");
    }
}
