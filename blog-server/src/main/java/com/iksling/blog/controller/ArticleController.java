package com.iksling.blog.controller;

import com.iksling.blog.annotation.OptLog;
import com.iksling.blog.pojo.Result;
import com.iksling.blog.service.ArticleService;
import com.iksling.blog.vo.ArticleBackVO;
import com.iksling.blog.vo.StatusBackVO;
import com.iksling.blog.vo.ConditionBackVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.iksling.blog.constant.LogConst.*;

@RestController
@Api(tags = "文章模块")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

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
        return Result.success().message("查询成功").data(articleService.getArticleOptionBackDTO(userId));
    }

    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "添加或修改文章")
    @ApiImplicitParam(name = "articleBackVO", value = "文章后台VO", required = true, dataType = "ArticleBackVO")
    @PostMapping("/back/article")
    public Result saveOrUpdateBackArticle(@Valid @RequestBody ArticleBackVO articleBackVO) {
        return Result.success().message("操作成功").data(articleService.saveOrUpdateArticleBackVO(articleBackVO));
    }

    @ApiOperation(value = "查看后台文章列表")
    @ApiImplicitParam(name = "condition", value = "查询条件", required = true, dataType = "ConditionBackVO")
    @GetMapping("/back/articles")
    public Result getBackArticles(@Valid ConditionBackVO condition) {
        return Result.success().message("查询成功").data(articleService.getArticlesBackDTO(condition));
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "批量更新文章状态")
    @ApiImplicitParam(name = "StatusBackVO", value = "状态后台VO", required = true, dataType = "StatusBackVO")
    @PutMapping("/back/articles/status")
    public Result updateBackArticlesStatus(@Valid @RequestBody StatusBackVO statusBackVO) {
        articleService.updateArticlesStatusBackVO(statusBackVO);
        return Result.success().message("操作成功");
    }

    @OptLog(optType = REMOVE)
    @ApiOperation(value = "物理批量删除文章")
    @ApiImplicitParam(name = "idList", value = "文章idList", required = true, dataType = "List<Integer>")
    @DeleteMapping("/back/articles")
    public Result deleteBackArticlesByIdList(@RequestBody List<Integer> idList) {
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
}
