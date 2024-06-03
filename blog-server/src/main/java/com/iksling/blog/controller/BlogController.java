package com.iksling.blog.controller;

import com.iksling.blog.annotation.OptLog;
import com.iksling.blog.pojo.Result;
import com.iksling.blog.service.BlogService;
import com.iksling.blog.vo.TokenVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.iksling.blog.constant.LogConst.UPDATE;

@RestController
@Api(tags = "博客模块")
public class BlogController {
    @Autowired
    private BlogService blogService;

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "修改关于我")
    @ApiImplicitParam(name = "aboutContent", value = "关于我", required = true, dataType = "String")
    @PutMapping("/back/about")
    public Result updateBackAbout(@RequestBody String aboutContent) {
        blogService.updateBackAbout(aboutContent);
        return Result.success().message("操作成功");
    }

    /****************************************************************************************************/

    @ApiOperation(value = "校验访问令牌")
    @ApiImplicitParam(name = "tokenVO", value = "令牌VO", required = true, dataType = "TokenVO")
    @PostMapping("/blog/token")
    public Result saveBlogTokenVO(@Valid @RequestBody TokenVO tokenVO) {
        return Result.success().message("操作成功").data(blogService.saveTokenVO(tokenVO));
    }

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

    @ApiOperation(value = "查看关于我")
    @GetMapping("/about")
    public Result getAbout() {
        return Result.success().message("查询成功").data(blogService.getAbout());
    }
}
