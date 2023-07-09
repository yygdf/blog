package com.iksling.blog.controller;

import com.iksling.blog.annotation.OptLog;
import com.iksling.blog.pojo.Result;
import com.iksling.blog.service.ArticleService;
import com.iksling.blog.service.MultiFileService;
import com.iksling.blog.vo.ArticleBackVO;
import com.iksling.blog.vo.ArticlesGarbageVO;
import com.iksling.blog.vo.ConditionVO;
import com.iksling.blog.vo.MultiFileArticleBackVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.iksling.blog.constant.OptLogConst.*;

@RestController
@Api(tags = "文章模块")
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private MultiFileService multiFileService;

    @ApiOperation(value = "根据文章id查找文章")
    @ApiImplicitParam(name = "articleId", value = "文章id", required = true, dataType = "Integer")
    @GetMapping("/back/article/{articleId}")
    public Result getBackArticleById(@PathVariable Integer articleId) {
        return Result.success().message("查询成功").data(articleService.getArticleBackDTOById(articleId));
    }

    @ApiOperation(value = "查看文章选项")
    @GetMapping("/back/article/options")
    public Result listBackArticleOptions() {
        return Result.success().message("查询成功").data(articleService.getArticleOptionDTO());
    }

    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "添加或修改文章")
    @PostMapping("/back/article")
    public Result saveBackArticle(@Valid @RequestBody ArticleBackVO articleBackVO) {
        Integer id = articleService.saveOrUpdateArticleBackVO(articleBackVO);
        return Result.success().message("操作成功").data(id);
    }

    @OptLog(optType = UPLOAD)
    @ApiOperation(value = "上传文章图片")
    @ApiImplicitParam(name = "file", value = "文章图片", required = true, dataType = "MultipartFile")
    @PostMapping("/back/article/image")
    public Result saveBackArticleImage(@Valid MultiFileArticleBackVO multiFileArticleBackVO) {
        String url = multiFileService.saveMultiFileArticleBackVO(multiFileArticleBackVO);
        return Result.success().message("上传成功").data(url);
    }

    @ApiOperation(value = "查看后台文章列表")
    @GetMapping("/back/articles")
    public Result listBackArticles(ConditionVO condition) {
        return Result.success().message("查询成功").data(articleService.getPageArticlesBackDTO(condition));
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "批量更新垃圾文章")
    @PutMapping("/back/articles")
    public Result updateBackArticles(ArticlesGarbageVO articlesGarbageVO) {
        articleService.updateArticlesGarbageVO(articlesGarbageVO);
        return Result.success().message("操作成功");
    }

    @OptLog(optType = REMOVE)
    @ApiOperation(value = "批量删除文章")
    @DeleteMapping("/back/articles")
    public Result deleteBackArticles(@RequestBody List<Integer> articleIdList) {
        articleService.deleteArticleIdList(articleIdList);
        return Result.success().message("操作成功");
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "修改文章置顶")
    @PutMapping("/back/article/top/{articleId}")
    public Result updateArticleTop(@PathVariable("articleId") Integer articleId, Boolean topFlag) {
        articleService.updateArticleTopById(articleId, topFlag);
        return Result.success().message("操作成功");
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "修改文章是否公开")
    @PutMapping("/back/article/public/{articleId}")
    public Result updateArticlePublic(@PathVariable("articleId") Integer articleId, Boolean publicFlag) {
        articleService.updateArticlePublicById(articleId, publicFlag);
        return Result.success().message("操作成功");
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "修改文章是否隐藏")
    @PutMapping("/back/article/hidden/{articleId}")
    public Result updateArticleHidden(@PathVariable("articleId") Integer articleId, Boolean hiddenFlag) {
        articleService.updateArticleHiddenById(articleId, hiddenFlag);
        return Result.success().message("操作成功");
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "修改文章是否可评论")
    @PutMapping("/back/article/commentable/{articleId}")
    public Result updateArticleCommentable(@PathVariable("articleId") Integer articleId, Boolean commentableFlag) {
        articleService.updateArticleCommentableById(articleId, commentableFlag);
        return Result.success().message("操作成功");
    }

    @OptLog(optType = REMOVE)
    @ApiOperation(value = "删除文章图片")
    @DeleteMapping("/back/article/image")
    public Result deleteBackArticleImage(String url) {
        multiFileService.deleteArticleImageByUrl(url);
        return Result.success().message("操作成功");
    }
}
