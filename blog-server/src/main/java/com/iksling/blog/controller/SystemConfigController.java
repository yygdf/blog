package com.iksling.blog.controller;

import com.iksling.blog.annotation.OptLog;
import com.iksling.blog.pojo.Result;
import com.iksling.blog.service.SystemConfigService;
import com.iksling.blog.vo.ConditionBackVO;
import com.iksling.blog.vo.SystemConfigBackVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.iksling.blog.constant.LogConst.REMOVE;
import static com.iksling.blog.constant.LogConst.SAVE_OR_UPDATE;

@RestController
@Api(tags = "系统配置模块")
public class SystemConfigController {
    @Autowired
    private SystemConfigService systemConfigService;

    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "添加或修改系统配置")
    @ApiImplicitParam(name = "systemConfigBackVO", value = "系统配置后台VO", required = true, dataType = "SystemConfigBackVO")
    @PostMapping("/back/systemConfig")
    public Result saveOrUpdateBackSystemConfig(@Valid @RequestBody SystemConfigBackVO systemConfigBackVO) {
        systemConfigService.saveOrUpdateSystemConfigBackVO(systemConfigBackVO);
        return Result.success().message("操作成功");
    }

    @OptLog(optType = REMOVE)
    @ApiOperation(value = "物理批量删除系统配置")
    @ApiImplicitParam(name = "idList", value = "idList", required = true, dataType = "List<Integer>")
    @DeleteMapping("/back/systemConfigs")
    public Result deleteBackSystemConfigs(@RequestBody List<Integer> idList) {
        systemConfigService.deleteBackSystemConfigsByIdList(idList);
        return Result.success().message("操作成功");
    }

    @ApiOperation(value = "查看后台系统配置列表")
    @ApiImplicitParam(name = "condition", value = "查询条件", required = true, dataType = "ConditionBackVO")
    @GetMapping("/back/systemConfigs")
    public Result getBackSystemConfigs(@Valid ConditionBackVO condition) {
        return Result.success().message("查询成功").data(systemConfigService.getSystemConfigsBackDTO(condition));
    }
}
