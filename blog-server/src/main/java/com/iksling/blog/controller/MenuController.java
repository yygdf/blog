package com.iksling.blog.controller;

import com.iksling.blog.pojo.Result;
import com.iksling.blog.service.MenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "菜单模块")
public class MenuController {
    @Autowired
    private MenuService menuService;

    @ApiOperation(value = "查看用户菜单")
    @GetMapping("/back/user/menus")
    public Result listBackUserMenus() {
        return Result.success().message("查询成功").data(menuService.listUserMenuDTO());
    }
}
