package com.iksling.blog.controller;

import com.iksling.blog.annotation.OptLog;
import com.iksling.blog.pojo.Dict;
import com.iksling.blog.pojo.Result;
import com.iksling.blog.service.UserConfigService;
import com.iksling.blog.vo.ConditionBackVO;
import com.iksling.blog.vo.StatusBackVO;
import com.iksling.blog.vo.UserConfigBackVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.iksling.blog.constant.CommonConst.ROOT_USER_ID;
import static com.iksling.blog.constant.CommonConst.ROOT_USER_ID_LIST;
import static com.iksling.blog.constant.LogConst.UPDATE;

@RestController
@Api(tags = "用户配置模块")
public class UserConfigController {
    @Autowired
    private UserConfigService userConfigService;

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "修改用户配置")
    @ApiImplicitParam(name = "userConfigBackVO", value = "用户配置后台VO", required = true, dataType = "UserConfigBackVO")
    @PutMapping("/back/userConfig")
    public Result updateBackUserConfig(@Valid @RequestBody UserConfigBackVO userConfigBackVO) {
        userConfigService.updateUserConfigBackVO(userConfigBackVO);
        return Result.success().message("操作成功");
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "批量更新用户配置状态")
    @ApiImplicitParam(name = "statusBackVO", value = "状态后台VO", required = true, dataType = "StatusBackVO")
    @PutMapping("/back/userConfigs/status")
    public Result updateBackUserConfigsStatus(@Valid @RequestBody StatusBackVO statusBackVO) {
        userConfigService.updateUserConfigsStatusBackVO(statusBackVO);
        return Result.success().message("操作成功");
    }

    @ApiOperation(value = "查看后台用户配置列表")
    @ApiImplicitParam(name = "condition", value = "查询条件", required = true, dataType = "ConditionBackVO")
    @GetMapping("/back/userConfigs")
    public Result getBackUserConfigs(@Valid ConditionBackVO condition) {
        return Result.success().message("查询成功").data(Dict.create()
                .set("rootUserId", ROOT_USER_ID)
                .set("rootUserIdList", ROOT_USER_ID_LIST)
                .set("pagePojo", userConfigService.getUserConfigsBackDTO(condition)));
    }
}
