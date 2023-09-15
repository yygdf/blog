package com.iksling.blog.controller;

import com.iksling.blog.annotation.OptLog;
import com.iksling.blog.pojo.Dict;
import com.iksling.blog.pojo.Result;
import com.iksling.blog.service.UserAuthService;
import com.iksling.blog.vo.CommonStatusVO;
import com.iksling.blog.vo.ConditionVO;
import com.iksling.blog.vo.UpdateBatchVO;
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
import static com.iksling.blog.constant.OptLogConst.UPDATE;

@RestController
@Api(tags = "账号模块")
public class UserAuthController {
    @Autowired
    private UserAuthService userAuthService;

    @ApiOperation(value = "查看所有的用户名")
    @ApiImplicitParam(name = "keywords", value = "用户名关键字", required = true, dataType = "String")
    @GetMapping("/back/userAuth/usernames")
    public Result listBackUsernames(String keywords) {
        return Result.success().message("查询成功").data(userAuthService.getBackUsernames(keywords));
    }

    @ApiOperation(value = "查看后台账号列表")
    @ApiImplicitParam(name = "condition", value = "查询条件", required = true, dataType = "ConditionVO")
    @GetMapping("/back/userAuths")
    public Result listBackUserAuths(@Valid ConditionVO condition) {
        return Result.success().message("查询成功").data(Dict.create()
                .set("rootRoleIdList", ROOT_ROLE_ID_LIST)
                .set("pagePojo", userAuthService.getPageUserAuthsBackDTO(condition)));
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "修改账号状态")
    @ApiImplicitParam(name = "commonStatusVO", value = "通用状态VO", required = true, dataType = "CommonStatusVO")
    @PutMapping("/back/userAuth/status")
    public Result updateUserAuthStatus(@Valid @RequestBody CommonStatusVO commonStatusVO) {
        userAuthService.updateUserAuthStatusVO(commonStatusVO);
        return Result.success().message("操作成功");
    }

    @ApiOperation(value = "查看账号是否存在")
    @ApiImplicitParam(name = "keywords", value = "用户名关键字", required = true, dataType = "String")
    @GetMapping("/back/userAuth/exist")
    public Result getBackUserAuthExistFlag(String keywords) {
        return Result.success().message("查询成功").data(userAuthService.getBackUserAuthExistFlag(keywords));
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "修改账号信息")
    @ApiImplicitParam(name = "userAuthBackVO", value = "账号后台VO", required = true, dataType = "UserAuthBackVO")
    @PutMapping("/back/userAuth")
    public Result updateBackUserAuth(@Valid @RequestBody UserAuthBackVO userAuthBackVO) {
        userAuthService.updateUserAuthBackVO(userAuthBackVO);
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
