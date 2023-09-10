package com.iksling.blog.controller;

import com.iksling.blog.annotation.OptLog;
import com.iksling.blog.pojo.Result;
import com.iksling.blog.service.SystemConfigService;
import com.iksling.blog.vo.ConditionVO;
import com.iksling.blog.vo.ConfigBackVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.iksling.blog.constant.OptLogConst.REMOVE;
import static com.iksling.blog.constant.OptLogConst.SAVE_OR_UPDATE;

@RestController
@Api(tags = "系统配置模块")
public class SystemConfigController {
    @Autowired
    private SystemConfigService systemConfigService;

    @ApiOperation(value = "查看后台系统配置列表")
    @ApiImplicitParam(name = "condition", value = "查询条件", required = true, dataType = "ConditionVO")
    @GetMapping("/back/systemConfigs")
    public Result listBackSystemConfigs(@Valid ConditionVO condition) {
        return Result.success().message("查询成功").data(systemConfigService.getPageSystemConfigsBackDTO(condition));
    }

    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "添加或修改系统配置")
    @ApiImplicitParam(name = "systemConfigBackVO", value = "系统配置后台VO", required = true, dataType = "SystemConfigBackVO")
    @PostMapping("/back/systemConfig")
    public Result saveOrUpdateBackSystemConfig(@Valid @RequestBody ConfigBackVO configBackVO) {
        systemConfigService.saveOrUpdateSystemConfigBackVO(configBackVO);
        return Result.success().message("操作成功");
    }

    @OptLog(optType = REMOVE)
    @ApiOperation(value = "删除系统配置")
    @ApiImplicitParam(name = "id", value = "系统配置id", required = true, dataType = "String")
    @DeleteMapping("/back/systemConfig")
    public Result deleteBackSystemConfig(@RequestBody String id) {
        systemConfigService.deleteSystemConfigById(id);
        return Result.success().message("操作成功");
    }
}
