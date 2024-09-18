package com.iksling.blog.controller;

import com.iksling.blog.annotation.OptLog;
import com.iksling.blog.pojo.Result;
import com.iksling.blog.service.BlogService;
import com.iksling.blog.vo.StatusBackVO;
import com.iksling.blog.vo.TokenVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

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

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "修改后台用户消息提醒设置")
    @ApiImplicitParam(name = "StatusBackVO", value = "状态后台VO", required = true, dataType = "StatusBackVO")
    @PutMapping("/back/blog/messageConfig")
    public Result updateBackMessageConfig(@Valid @RequestBody StatusBackVO statusBackVO) {
        blogService.updateBackBlogMessageConfig(statusBackVO);
        return Result.success().message("操作成功");
    }

    @ApiOperation(value = "查看后台用户消息提醒设置")
    @GetMapping("/back/blog/messageConfig")
    public Result getBackMessageConfigs() {
        return Result.success().message("查询成功").data(blogService.getBackBlogMessageConfig());
    }

    @ApiOperation(value = "查看文章统计信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "Integer"),
            @ApiImplicitParam(name = "endDate", value = "结束日期", dataType = "Date"),
            @ApiImplicitParam(name = "days", value = "天数", dataType = "Integer")
    })
    @GetMapping("/back/blog/articleStatistic")
    public Result getBackArticleStatistic(Integer userId, Date endDate, Integer days) {
        return Result.success().message("查询成功").data(blogService.getBackArticleStatistic(userId, endDate, days));
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
