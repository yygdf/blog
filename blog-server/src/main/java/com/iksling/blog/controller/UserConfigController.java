package com.iksling.blog.controller;

import com.iksling.blog.annotation.OptLog;
import com.iksling.blog.pojo.Condition;
import com.iksling.blog.pojo.Dict;
import com.iksling.blog.pojo.Result;
import com.iksling.blog.service.UserConfigService;
import com.iksling.blog.util.LocaleUtil;
import com.iksling.blog.vo.StatusBackVO;
import com.iksling.blog.vo.UserConfigBackVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
        return Result.success().message(LocaleUtil.getMessage("C0001"));
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "批量更新用户配置状态")
    @ApiImplicitParam(name = "statusBackVO", value = "状态后台VO", required = true, dataType = "StatusBackVO")
    @PutMapping("/back/userConfigs/status")
    public Result updateBackUserConfigsStatus(@Valid @RequestBody StatusBackVO statusBackVO) {
        userConfigService.updateUserConfigsStatusBackVO(statusBackVO);
        return Result.success().message(LocaleUtil.getMessage("C0001"));
    }

    @ApiOperation(value = "查看后台用户配置列表")
    @ApiImplicitParam(name = "condition", value = "查询条件", required = true, dataType = "Condition")
    @GetMapping("/back/userConfigs")
    public Result getBackUserConfigs(@Valid Condition condition) {
        return Result.success().message(LocaleUtil.getMessage("C0003")).data(Dict.create()
                .set("rootUserId", ROOT_USER_ID)
                .set("rootUserIdList", ROOT_USER_ID_LIST)
                .set("pagePojo", userConfigService.getUserConfigsBackDTO(condition)));
    }
}
