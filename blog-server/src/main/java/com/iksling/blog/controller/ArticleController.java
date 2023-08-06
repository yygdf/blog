package com.iksling.blog.controller;

import com.iksling.blog.annotation.OptLog;
import com.iksling.blog.pojo.Result;
import com.iksling.blog.service.ArticleService;
import com.iksling.blog.vo.ArticleBackVO;
import com.iksling.blog.vo.CommonStatusVO;
import com.iksling.blog.vo.UpdateBatchVO;
import com.iksling.blog.vo.ConditionVO;
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

    @ApiOperation(value = "根据文章id查找文章")
    @ApiImplicitParam(name = "articleId", value = "文章id", required = true, dataType = "Integer")
    @GetMapping("/back/article/{articleId}")
    public Result getBackArticleById(@PathVariable Integer articleId) {
        return Result.success().message("查询成功").data(articleService.getArticleBackDTOById(articleId));
    }

    @ApiOperation(value = "查看文章选项")
    @ApiImplicitParam(name = "userId", value = "用户id", dataType = "Integer")
    @GetMapping("/back/article/options")
    public Result listBackArticleOptions(Integer userId) {
        return Result.success().message("查询成功").data(articleService.getArticleOptionDTO(userId));
    }

    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "添加或修改文章")
    @ApiImplicitParam(name = "articleBackVO", value = "文章后台VO", required = true, dataType = "ArticleBackVO")
    @PostMapping("/back/article")
    public Result saveBackArticle(@Valid @RequestBody ArticleBackVO articleBackVO) {
        Integer id = articleService.saveOrUpdateArticleBackVO(articleBackVO);
        return Result.success().message("操作成功").data(id);
    }

    @ApiOperation(value = "查看后台文章列表")
    @ApiImplicitParam(name = "condition", value = "查询条件", required = true, dataType = "ConditionVO")
    @GetMapping("/back/articles")
    public Result listBackArticles(@Valid ConditionVO condition) {
        return Result.success().message("查询成功").data(articleService.getPageArticlesBackDTO(condition));
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "批量更新文章状态")
    @ApiImplicitParam(name = "updateBatchVO", value = "批量更新VO", required = true, dataType = "UpdateBatchVO")
    @PutMapping("/back/articles")
    public Result updateArticlesStatus(@Valid UpdateBatchVO updateBatchVO) {
        articleService.updateArticlesStatus(updateBatchVO);
        return Result.success().message("操作成功");
    }

    @OptLog(optType = REMOVE)
    @ApiOperation(value = "批量删除文章")
    @ApiImplicitParam(name = "articleIdList", value = "文章idList", required = true, dataType = "List<Integer>")
    @DeleteMapping("/back/articles")
    public Result deleteBackArticles(@RequestBody List<Integer> articleIdList) {
        articleService.deleteArticleIdList(articleIdList);
        return Result.success().message("操作成功");
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "修改文章状态")
    @ApiImplicitParam(name = "commonStatusVO", value = "通用状态VO", required = true, dataType = "CommonStatusVO")
    @PutMapping("/back/article/status")
    public Result updateArticleStatus(@Valid @RequestBody CommonStatusVO commonStatusVO) {
        articleService.updateArticleStatusVO(commonStatusVO);
        return Result.success().message("操作成功");
    }
}
