package com.iksling.blog.controller;

import com.iksling.blog.annotation.OptLog;
import com.iksling.blog.pojo.Result;
import com.iksling.blog.service.UserConfigService;
import com.iksling.blog.vo.ConditionVO;
import com.iksling.blog.vo.ConfigBackVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.iksling.blog.constant.OptLogConst.*;

@RestController
@Api(tags = "用户配置模块")
public class UserConfigController {
    @Autowired
    private UserConfigService userConfigService;

    @ApiOperation(value = "查看后台用户配置列表")
    @ApiImplicitParam(name = "condition", value = "查询条件", required = true, dataType = "ConditionVO")
    @GetMapping("/back/userConfigs")
    public Result listBackUserConfigs(@Valid ConditionVO condition) {
        return Result.success().message("查询成功").data(userConfigService.getPageUserConfigsBackDTO(condition));
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "更新用户配置状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "configName", value = "用户配置名", required = true, dataType = "String")
    })
    @PutMapping("/back/userConfig")
    public Result updateUserConfigsStatus(@RequestBody Integer userId, @RequestBody String configName) {
        userConfigService.updateUserConfigsStatus(userId, configName);
        return Result.success().message("操作成功");
    }

    @OptLog(optType = REMOVE)
    @ApiOperation(value = "删除用户配置")
    @ApiImplicitParam(name = "configName", value = "用户配置名", required = true, dataType = "String")
    @DeleteMapping("/back/userConfig")
    public Result deleteBackUserConfig(@RequestBody String configName) {
        userConfigService.deleteUserConfigByConfigName(configName);
        return Result.success().message("操作成功");
    }

    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "添加或修改用户配置")
    @ApiImplicitParam(name = "userConfigBackVO", value = "用户配置后台VO", required = true, dataType = "UserConfigBackVO")
    @PostMapping("/back/userConfig")
    public Result saveOrUpdateBackUserConfig(@Valid @RequestBody ConfigBackVO configBackVO) {
        userConfigService.saveOrUpdateUserConfigBackVO(configBackVO);
        return Result.success().message("操作成功");
    }
}
