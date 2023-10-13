package com.iksling.blog.controller;

import com.iksling.blog.pojo.Result;
import com.iksling.blog.service.OperationLogService;
import com.iksling.blog.vo.ConditionVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Api(tags = "操作日志模块")
public class OperationLogController {
    @Autowired
    private OperationLogService operationLogService;

    @ApiOperation(value = "查看后台操作日志列表")
    @ApiImplicitParam(name = "condition", value = "查询条件", required = true, dataType = "ConditionVO")
    @GetMapping("/back/operationLogs")
    public Result listBackOperationLogs(@Valid ConditionVO condition) {
        return Result.success().message("查询成功").data(operationLogService.getPageOperationLogsBackDTO(condition));
    }
}
