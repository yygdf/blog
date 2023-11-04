package com.iksling.blog.controller;

import com.iksling.blog.annotation.OptLog;
import com.iksling.blog.pojo.Dict;
import com.iksling.blog.pojo.Result;
import com.iksling.blog.service.UserAuthService;
import com.iksling.blog.vo.ConditionBackVO;
import com.iksling.blog.vo.StatusBackVO;
import com.iksling.blog.vo.UserAuthBackVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.iksling.blog.constant.CommonConst.ROOT_ROLE_ID_LIST;
import static com.iksling.blog.constant.CommonConst.ROOT_USER_ID_LIST;
import static com.iksling.blog.constant.LogConst.UPDATE;

@RestController
@Api(tags = "账号模块")
public class UserAuthController {
    @Autowired
    private UserAuthService userAuthService;

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "修改账号")
    @ApiImplicitParam(name = "userAuthBackVO", value = "账号后台VO", required = true, dataType = "UserAuthBackVO")
    @PutMapping("/back/userAuth")
    public Result updateBackUserAuth(@Valid @RequestBody UserAuthBackVO userAuthBackVO) {
        userAuthService.updateUserAuthBackVO(userAuthBackVO);
        return Result.success().message("操作成功");
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "修改账号状态")
    @ApiImplicitParam(name = "statusBackVO", value = "状态后台VO", required = true, dataType = "StatusBackVO")
    @PutMapping("/back/userAuth/status")
    public Result updateBackUserAuthStatus(@Valid @RequestBody StatusBackVO statusBackVO) {
        userAuthService.updateUserAuthStatusBackVO(statusBackVO);
        return Result.success().message("操作成功");
    }

    @ApiOperation(value = "查看后台账号列表")
    @ApiImplicitParam(name = "condition", value = "查询条件", required = true, dataType = "ConditionBackVO")
    @GetMapping("/back/userAuths")
    public Result getBackUserAuths(@Valid ConditionBackVO condition) {
        return Result.success().message("查询成功").data(Dict.create()
                .set("rootUserIdList", ROOT_USER_ID_LIST)
                .set("rootRoleIdList", ROOT_ROLE_ID_LIST)
                .set("pagePojo", userAuthService.getUserAuthsBackDTO(condition)));
    }

    @ApiOperation(value = "查看所有的用户名")
    @ApiImplicitParam(name = "keywords", value = "关键字(用户名)", required = true, dataType = "String")
    @GetMapping("/back/userAuth/usernames")
    public Result getBackUsernames(String keywords) {
        return Result.success().message("查询成功").data(userAuthService.getBackUsernames(keywords));
    }
}
