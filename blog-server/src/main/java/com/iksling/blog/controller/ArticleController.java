package com.iksling.blog.controller;

import com.iksling.blog.pojo.Result;
import com.iksling.blog.service.ArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "文章模块")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @ApiOperation(value = "根据文章id查找文章")
    @ApiImplicitParam(name = "articleId", value = "文章id", required = true, dataType = "Integer")
    @GetMapping("/back/articles/{articleId}")
    public Result getBackArticleById(@PathVariable Integer articleId) {
        return Result.success().message("查询成功").data(articleService.getBackArticleById(articleId));
    }
}
