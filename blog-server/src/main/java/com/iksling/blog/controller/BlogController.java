package com.iksling.blog.controller;

import com.iksling.blog.pojo.Result;
import com.iksling.blog.service.BlogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "博客模块")
public class BlogController {
    @Autowired
    private BlogService blogService;

    @ApiOperation(value = "查看博主id")
    @ApiImplicitParam(name = "bloggerId", value = "博主id", dataType = "Integer")
    @GetMapping("/blog/id")
    public Result getBlogId(Integer bloggerId) {
        return Result.success().message("查询成功").data(blogService.getBlogId(bloggerId));
    }

    @ApiOperation(value = "查看博客信息")
    @GetMapping("/blog/info")
    public Result getBlogInfo() {
        return Result.success().message("查询成功").data(blogService.getBlogInfo());
    }
}
