package com.iksling.blog.controller;

import com.iksling.blog.annotation.OptLog;
import com.iksling.blog.pojo.Dict;
import com.iksling.blog.pojo.Result;
import com.iksling.blog.service.ArticleService;
import com.iksling.blog.vo.ArticleBackVO;
import com.iksling.blog.vo.ArticleImageBackVO;
import com.iksling.blog.pojo.Condition;
import com.iksling.blog.vo.StatusBackVO;
import com.iksling.blog.vo.TokenBackVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.iksling.blog.constant.CommonConst.STATIC_RESOURCE_URL;
import static com.iksling.blog.constant.LogConst.*;

@RestController
@Api(tags = "文章模块")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "添加或修改文章")
    @ApiImplicitParam(name = "articleBackVO", value = "文章后台VO", required = true, dataType = "ArticleBackVO")
    @PostMapping("/back/article")
    public Result saveOrUpdateBackArticle(@Valid @RequestBody ArticleBackVO articleBackVO) {
        return Result.success().message("操作成功").data(articleService.saveOrUpdateArticleBackVO(articleBackVO));
    }

    @OptLog(optType = SAVE)
    @ApiOperation(value = "上传文章图片")
    @ApiImplicitParam(name = "articleImageBackVO", value = "文章图片后台VO", required = true, dataType = "ArticleImageBackVO")
    @PostMapping("/back/article/image")
    public Result saveBackArticleImage(@Valid ArticleImageBackVO articleImageBackVO) {
        return Result.success().message("上传成功").data(articleService.saveArticleImageBackVO(articleImageBackVO));
    }

    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "添加或修改文章令牌")
    @ApiImplicitParam(name = "tokenBackVO", value = "令牌后台VO", required = true, dataType = "TokenBackVO")
    @PostMapping("/back/article/token")
    public Result saveOrUpdateBackArticleToken(@Valid @RequestBody TokenBackVO tokenBackVO) {
        articleService.saveOrUpdateArticleTokenBackVO(tokenBackVO);
        return Result.success().message("操作成功");
    }

    @OptLog(optType = REMOVE)
    @ApiOperation(value = "物理批量删除文章")
    @ApiImplicitParam(name = "idList", value = "idList", required = true, dataType = "List<Integer>")
    @DeleteMapping("/back/articles")
    public Result deleteBackArticles(@RequestBody List<Integer> idList) {
        articleService.deleteBackArticlesByIdList(idList);
        return Result.success().message("操作成功");
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "修改文章状态")
    @ApiImplicitParam(name = "StatusBackVO", value = "状态后台VO", required = true, dataType = "StatusBackVO")
    @PutMapping("/back/article/status")
    public Result updateBackArticleStatus(@Valid @RequestBody StatusBackVO statusBackVO) {
        articleService.updateArticleStatusBackVO(statusBackVO);
        return Result.success().message("操作成功");
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "批量更新文章状态")
    @ApiImplicitParam(name = "StatusBackVO", value = "状态后台VO", required = true, dataType = "StatusBackVO")
    @PutMapping("/back/articles/status")
    public Result updateBackArticlesStatus(@Valid @RequestBody StatusBackVO statusBackVO) {
        articleService.updateArticlesStatusBackVO(statusBackVO);
        return Result.success().message("操作成功");
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "批量更新文章图片状态")
    @ApiImplicitParam(name = "fileNameList", value = "文件名list", required = true, dataType = "List<Long>")
    @PutMapping("/back/article/images")
    public Result updateBackArticleImages(@RequestBody List<Long> fileNameList) {
        articleService.updateBackArticleImagesByFileNameList(fileNameList);
        return Result.success().message("操作成功");
    }

    @ApiOperation(value = "根据文章id查找文章")
    @ApiImplicitParam(name = "id", value = "文章id", required = true, dataType = "Integer")
    @GetMapping("/back/article/{id}")
    public Result getBackArticleById(@PathVariable Integer id) {
        return Result.success().message("查询成功").data(articleService.getArticleBackDTOById(id));
    }

    @ApiOperation(value = "查看文章选项")
    @ApiImplicitParam(name = "userId", value = "用户id", dataType = "Integer")
    @GetMapping("/back/article/option")
    public Result getBackArticleOption(Integer userId) {
        return Result.success().message("查询成功").data(Dict.create()
                .set("option", articleService.getArticleOptionBackDTO(userId))
                .set("staticResourceUrl", STATIC_RESOURCE_URL));
    }

    @ApiOperation(value = "查看后台文章列表")
    @ApiImplicitParam(name = "condition", value = "查询条件", required = true, dataType = "Condition")
    @GetMapping("/back/articles")
    public Result getBackArticles(@Valid Condition condition) {
        return Result.success().message("查询成功").data(articleService.getArticlesBackDTO(condition));
    }

    @ApiOperation(value = "根据文章id查找文章令牌")
    @ApiImplicitParam(name = "id", value = "文章id", required = true, dataType = "Integer")
    @GetMapping("/back/article/token/{id}")
    public Result getBackArticleTokenById(@PathVariable Integer id) {
        return Result.success().message("操作成功").data(articleService.getArticleTokenById(id));
    }

    /****************************************************************************************************/

    @OptLog(optType = SAVE)
    @ApiOperation(value = "点赞文章")
    @ApiImplicitParam(name = "id", value = "文章id", required = true, dataType = "Integer")
    @PostMapping("/article/like/{id}")
    public Result saveArticleLike(@PathVariable Integer id) {
        articleService.saveArticleLike(id);
        return Result.success().message("操作成功");
    }

    @ApiOperation(value = "查看文章列表")
    @ApiImplicitParam(name = "condition", value = "查询条件", required = true, dataType = "ConditionVO")
    @GetMapping("/articles")
    public Result getArticles(@Valid Condition condition) {
        return Result.success().message("查询成功").data(articleService.getArticlesDTO(condition));
    }

    @ApiOperation(value = "根据文章id查找文章")
    @ApiImplicitParam(name = "id", value = "文章id", required = true, dataType = "Integer")
    @GetMapping("/article/{id}")
    public Result getArticleById(@PathVariable Integer id) {
        return Result.success().message("查询成功").data(articleService.getArticleDTOById(id));
    }

    @ApiOperation(value = "查看最新文章列表")
    @GetMapping("/articles/newest")
    public Result getArticlesNewest() {
        return Result.success().message("查询成功").data(articleService.getArticlesRecommendDTO());
    }

    @ApiOperation(value = "预览文章列表")
    @ApiImplicitParam(name = "condition", value = "查询条件", required = true, dataType = "ConditionVO")
    @GetMapping("/articles/preview")
    public Result getArticlesPreview(@Valid Condition condition) {
        return Result.success().message("查询成功").data(articleService.getArticlesPreviewDTO(condition));
    }

    @ApiOperation(value = "查看文章归档")
    @GetMapping("/articles/archive")
    public Result getArticlesArchive(@Valid Condition condition) {
        return Result.success().message("查询成功").data(articleService.getArticlesArchiveDTO(condition));
    }

    @ApiOperation(value = "搜索文章列表")
    @ApiImplicitParam(name = "condition", value = "查询条件", required = true, dataType = "Condition")
    @GetMapping("/articles/search")
    public Result getArticlesSearch(@Valid Condition condition) {
        return Result.success().message("查询成功").data(articleService.getArticlesSearchDTO(condition));
    }
}
