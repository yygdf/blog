package com.iksling.blog.controller;

import com.iksling.blog.pojo.Result;
import com.iksling.blog.service.CategoryService;
import com.iksling.blog.vo.ConditionVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "分类模块")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @ApiOperation(value = "查看后台分类列表")
    @GetMapping("/back/categories")
    public Result listBackCategories(ConditionVO condition) {
        return Result.success().message("查询成功").data(categoryService.getPageCategoriesBackDTO(condition));
    }
}
