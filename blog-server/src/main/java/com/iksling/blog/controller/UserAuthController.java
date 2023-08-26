package com.iksling.blog.controller;

import com.iksling.blog.annotation.OptLog;
import com.iksling.blog.pojo.Result;
import com.iksling.blog.service.UserAuthService;
import com.iksling.blog.vo.CommonStatusVO;
import com.iksling.blog.vo.ConditionVO;
import com.iksling.blog.vo.UpdateBatchVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.iksling.blog.constant.OptLogConst.UPDATE;

@RestController
@Api(tags = "账号模块")
public class UserAuthController {
    @Autowired
    private UserAuthService userAuthService;

    @ApiOperation(value = "查看所有的用户名")
    @GetMapping("/back/userAuth/usernames")
    public Result listBackUsernames(String keywords) {
        return Result.success().message("查询成功").data(userAuthService.getBackUsernames(keywords));
    }

    @ApiOperation(value = "查看后台账号列表")
    @ApiImplicitParam(name = "condition", value = "查询条件", required = true, dataType = "ConditionVO")
    @GetMapping("/back/userAuths")
    public Result listBackUserAuths(@Valid ConditionVO condition) {
        return Result.success().message("查询成功").data(userAuthService.getPageUserAuthsBackDTO(condition));
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "修改账号状态")
    @ApiImplicitParam(name = "commonStatusVO", value = "通用状态VO", required = true, dataType = "CommonStatusVO")
    @PutMapping("/back/userAuth/status")
    public Result updateUserAuthStatus(@Valid @RequestBody CommonStatusVO commonStatusVO) {
        userAuthService.updateUserAuthStatusVO(commonStatusVO);
        return Result.success().message("操作成功");
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "批量更新账号状态")
    @ApiImplicitParam(name = "updateBatchVO", value = "批量更新VO", required = true, dataType = "UpdateBatchVO")
    @PutMapping("/back/userAuths")
    public Result updateUserAuthsStatus(@Valid UpdateBatchVO updateBatchVO) {
        userAuthService.updateUserAuthsStatus(updateBatchVO);
        return Result.success().message("操作成功");
    }
}
