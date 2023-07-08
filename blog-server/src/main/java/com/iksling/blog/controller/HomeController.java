package com.iksling.blog.controller;

import com.iksling.blog.pojo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "首页模块")
public class HomeController {
    @ApiOperation(value = "查看后台首页信息")
    @GetMapping("/back")
    public Result getBack() {
        return Result.success().message("查询成功");
    }
}
