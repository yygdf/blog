package com.iksling.blog.controller;

import com.iksling.blog.annotation.OptLog;
import com.iksling.blog.pojo.Result;
import com.iksling.blog.service.ArticleService;
import com.iksling.blog.service.MultiFileService;
import com.iksling.blog.vo.ArticleBackVO;
import com.iksling.blog.vo.MultiFileBackVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.iksling.blog.constant.OptLogConst.SAVE_OR_UPDATE;
import static com.iksling.blog.constant.OptLogConst.UPLOAD;

@RestController
@Api(tags = "文章模块")
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private MultiFileService multiFileService;

    @ApiOperation(value = "根据文章id查找文章")
    @ApiImplicitParam(name = "articleId", value = "文章id", required = true, dataType = "Integer")
    @GetMapping("/back/articles/{articleId}")
    public Result getBackArticleById(@PathVariable Integer articleId) {
        return Result.success().message("查询成功").data(articleService.getBackArticleById(articleId));
    }

    @ApiOperation(value = "查看文章选项")
    @GetMapping("/back/articles/options")
    public Result listArticleOptionDTO() {
        return Result.success().message("查询成功").data(articleService.listArticleOptionDTO());
    }

    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "添加或修改文章")
    @PostMapping("/back/articles")
    public Result saveArticle(@Valid @RequestBody ArticleBackVO articleBackVO) {
        Integer id = articleService.saveOrUpdateArticle(articleBackVO);
        return Result.success().message("操作成功").data(id);
    }

    @OptLog(optType = UPLOAD)
    @ApiOperation(value = "上传文章图片")
    @ApiImplicitParam(name = "file", value = "文章图片", required = true, dataType = "MultipartFile")
    @PostMapping("/back/articles/images")
    public Result saveArticleImages(MultiFileBackVO multiFileBackVO) {
        multiFileService.saveArticleImgInfo(multiFileBackVO);
        return Result.success().message("上传成功").data(multiFileBackVO.getFileUrl());
    }
}
