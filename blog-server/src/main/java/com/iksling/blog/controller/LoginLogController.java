package com.iksling.blog.controller;

import com.iksling.blog.pojo.Result;
import com.iksling.blog.service.LoginLogService;
import com.iksling.blog.pojo.Condition;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Api(tags = "登录日志模块")
public class LoginLogController {
    @Autowired
    private LoginLogService loginLogService;

    @ApiOperation(value = "查看后台登录日志列表")
    @ApiImplicitParam(name = "condition", value = "查询条件", required = true, dataType = "Condition")
    @GetMapping("/back/loginLogs")
    public Result getBackLoginLogs(@Valid Condition condition) {
        return Result.success().message("查询成功").data(loginLogService.getLoginLogsBackDTO(condition));
    }
}
