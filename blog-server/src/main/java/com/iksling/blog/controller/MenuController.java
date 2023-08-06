package com.iksling.blog.controller;

import com.iksling.blog.annotation.OptLog;
import com.iksling.blog.pojo.Result;
import com.iksling.blog.service.MenuService;
import com.iksling.blog.vo.CommonStatusVO;
import com.iksling.blog.vo.MenuBackVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.iksling.blog.constant.OptLogConst.*;

@RestController
@Api(tags = "菜单模块")
public class MenuController {
    @Autowired
    private MenuService menuService;

    @ApiOperation(value = "查看用户菜单")
    @GetMapping("/back/user/menus")
    public Result listBackUserMenus() {
        return Result.success().message("查询成功").data(menuService.getUserMenusDTO());
    }

    @ApiOperation(value = "查看后台菜单列表")
    @ApiImplicitParam(name = "keywords", value = "菜单名关键字", dataType = "String")
    @GetMapping("/back/menus")
    public Result listBackMenus(String keywords) {
        return Result.success().message("查询成功").data(menuService.getMenusBackDTO(keywords));
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "修改菜单状态")
    @ApiImplicitParam(name = "commonStatusVO", value = "通用状态VO", required = true, dataType = "CommonStatusVO")
    @PutMapping("/back/menu/status")
    public Result updateMenuStatus(@Valid @RequestBody CommonStatusVO commonStatusVO) {
        menuService.updateMenuStatusVO(commonStatusVO);
        return Result.success().message("操作成功");
    }

    @OptLog(optType = REMOVE)
    @ApiOperation(value = "删除菜单")
    @ApiImplicitParam(name = "id", value = "菜单id", required = true, dataType = "Integer")
    @DeleteMapping("/back/menu")
    public Result deleteBackMenu(@RequestBody String id) {
        menuService.deleteMenuById(id);
        return Result.success().message("操作成功");
    }

    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "添加或修改菜单")
    @ApiImplicitParam(name = "menuBackVO", value = "菜单后台VO", required = true, dataType = "MenuBackVO")
    @PostMapping("/back/menu")
    public Result saveBackMenu(@Valid @RequestBody MenuBackVO menuBackVO) {
        menuService.saveOrUpdateMenuBackVO(menuBackVO);
        return Result.success().message("操作成功");
    }
}
