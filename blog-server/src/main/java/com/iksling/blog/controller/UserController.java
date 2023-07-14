package com.iksling.blog.controller;

import com.iksling.blog.pojo.Result;
import com.iksling.blog.service.UserAuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "用户模块")
public class UserController {
    @Autowired
    private UserAuthService userAuthService;

    @ApiOperation(value = "查看所有的用户名")
    @GetMapping("/back/user/username")
    public Result listBackAllUsername() {
        return Result.success().message("查询成功").data(userAuthService.getAllUsername());
    }
}
