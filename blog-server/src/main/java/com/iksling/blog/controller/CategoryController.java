package com.iksling.blog.controller;

import com.iksling.blog.annotation.OptLog;
import com.iksling.blog.pojo.Result;
import com.iksling.blog.service.CategoryService;
import com.iksling.blog.vo.CategoryBackVO;
import com.iksling.blog.vo.CategoryStatusVO;
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
@Api(tags = "分类模块")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @ApiOperation(value = "查看后台分类列表")
    @ApiImplicitParam(name = "condition", value = "查询条件", required = true, dataType = "ConditionVO")
    @GetMapping("/back/categories")
    public Result listBackCategories(@Valid ConditionVO condition) {
        return Result.success().message("查询成功").data(categoryService.getPageCategoriesBackDTO(condition));
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "修改分类状态")
    @ApiImplicitParam(name = "categoryStatusVO", value = "分类状态VO", required = true, dataType = "CategoryStatusVO")
    @PutMapping("/back/category/status")
    public Result updateCategoryStatus(@Valid @RequestBody CategoryStatusVO categoryStatusVO) {
        categoryService.updateCategoryStatusVO(categoryStatusVO);
        return Result.success().message("操作成功");
    }

    @OptLog(optType = REMOVE)
    @ApiOperation(value = "批量删除分类")
    @ApiImplicitParam(name = "categoryIdList", value = "分类idList", required = true, dataType = "List<Integer>")
    @DeleteMapping("/back/categories")
    public Result deleteBackCategories(@RequestBody List<Integer> categoryIdList) {
        categoryService.deleteCategoryIdList(categoryIdList);
        return Result.success().message("操作成功");
    }

    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "添加或修改分类")
    @ApiImplicitParam(name = "categoryBackVO", value = "分类后台VO", required = true, dataType = "CategoryBackVO")
    @PostMapping("/back/category")
    public Result saveBackCategory(@Valid @RequestBody CategoryBackVO categoryBackVO) {
        categoryService.saveOrUpdateCategoryBackVO(categoryBackVO);
        return Result.success().message("操作成功");
    }
}
