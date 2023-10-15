package com.iksling.blog.controller;

import com.iksling.blog.pojo.Result;
import com.iksling.blog.service.ExceptionLogService;
import com.iksling.blog.vo.ConditionBackVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Api(tags = "异常日志模块")
public class ExceptionLogController {
    @Autowired
    private ExceptionLogService exceptionLogService;

    @ApiOperation(value = "查看后台异常日志列表")
    @ApiImplicitParam(name = "condition", value = "查询条件", required = true, dataType = "ConditionVO")
    @GetMapping("/back/exceptionLogs")
    public Result listBackExceptionLogs(@Valid ConditionBackVO condition) {
        return Result.success().message("查询成功").data(exceptionLogService.getPageExceptionLogsBackDTO(condition));
    }
}
