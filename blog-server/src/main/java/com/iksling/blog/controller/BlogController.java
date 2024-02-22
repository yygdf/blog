package com.iksling.blog.controller;

import com.iksling.blog.pojo.Dict;
import com.iksling.blog.pojo.Result;
import com.iksling.blog.service.BlogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.iksling.blog.constant.CommonConst.HOME_MENU_ID;

@RestController
@Api(tags = "前台模块")
public class BlogController {
    @Autowired
    private BlogService blogService;

    @ApiOperation(value = "查看前台信息")
    @ApiImplicitParam(name = "bloggerId", value = "博主id", dataType = "Integer")
    @GetMapping("/blog/info")
    public Result getBlogInfo(Integer bloggerId) {
        return Result.success().message("查询成功").data(blogService.getBlogInfo(bloggerId));
    }
}
