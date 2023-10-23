package com.iksling.blog.controller;

import com.iksling.blog.annotation.OptLog;
import com.iksling.blog.pojo.Result;
import com.iksling.blog.service.CategoryService;
import com.iksling.blog.vo.CategoryBackVO;
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
@Api(tags = "分类模块")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "添加或修改分类")
    @ApiImplicitParam(name = "categoryBackVO", value = "分类后台VO", required = true, dataType = "CategoryBackVO")
    @PostMapping("/back/category")
    public Result saveOrUpdateBackCategory(@Valid @RequestBody CategoryBackVO categoryBackVO) {
        categoryService.saveOrUpdateCategoryBackVO(categoryBackVO);
        return Result.success().message("操作成功");
    }

    @OptLog(optType = REMOVE)
    @ApiOperation(value = "物理批量删除分类")
    @ApiImplicitParam(name = "idList", value = "idList", required = true, dataType = "List<Integer>")
    @DeleteMapping("/back/categories")
    public Result deleteBackCategories(@RequestBody List<Integer> idList) {
        categoryService.deleteBackCategoriesByIdList(idList);
        return Result.success().message("操作成功");
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "修改分类状态")
    @ApiImplicitParam(name = "statusBackVO", value = "状态后台VO", required = true, dataType = "StatusBackVO")
    @PutMapping("/back/category/status")
    public Result updateBackCategoryStatus(@Valid @RequestBody StatusBackVO statusBackVO) {
        categoryService.updateCategoryStatusBackVO(statusBackVO);
        return Result.success().message("操作成功");
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "批量更新分类状态")
    @ApiImplicitParam(name = "statusBackVO", value = "状态后台VO", required = true, dataType = "StatusBackVO")
    @PutMapping("/back/categories/status")
    public Result updateBackCategoriesStatus(@Valid @RequestBody StatusBackVO statusBackVO) {
        categoryService.updateCategoriesStatusBackVO(statusBackVO);
        return Result.success().message("操作成功");
    }

    @ApiOperation(value = "查看后台分类列表")
    @ApiImplicitParam(name = "condition", value = "查询条件", required = true, dataType = "ConditionBackVO")
    @GetMapping("/back/categories")
    public Result getBackCategories(@Valid ConditionBackVO condition) {
        return Result.success().message("查询成功").data(categoryService.getCategoriesBackDTO(condition));
    }
}
