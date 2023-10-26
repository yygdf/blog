package com.iksling.blog.controller;

import com.iksling.blog.annotation.OptLog;
import com.iksling.blog.pojo.Dict;
import com.iksling.blog.pojo.Result;
import com.iksling.blog.service.MenuService;
import com.iksling.blog.vo.MenuBackVO;
import com.iksling.blog.vo.StatusBackVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.iksling.blog.constant.CommonConst.HOME_MENU_ID;
import static com.iksling.blog.constant.LogConst.*;

@RestController
@Api(tags = "菜单模块")
public class MenuController {
    @Autowired
    private MenuService menuService;

    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "添加或修改菜单")
    @ApiImplicitParam(name = "menuBackVO", value = "菜单后台VO", required = true, dataType = "MenuBackVO")
    @PostMapping("/back/menu")
    public Result saveOrUpdateBackMenu(@Valid @RequestBody MenuBackVO menuBackVO) {
        menuService.saveOrUpdateMenuBackVO(menuBackVO);
        return Result.success().message("操作成功");
    }

    @OptLog(optType = REMOVE)
    @ApiOperation(value = "物理批量删除菜单")
    @ApiImplicitParam(name = "idList", value = "idList", required = true, dataType = "List<Integer>")
    @DeleteMapping("/back/menus")
    public Result deleteBackMenus(@RequestBody List<Integer> idList) {
        menuService.deleteBackMenusByIdList(idList);
        return Result.success().message("操作成功");
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "修改菜单状态")
    @ApiImplicitParam(name = "statusBackVO", value = "状态后台VO", required = true, dataType = "StatusBackVO")
    @PutMapping("/back/menu/status")
    public Result updateBackMenuStatus(@Valid @RequestBody StatusBackVO statusBackVO) {
        menuService.updateMenuStatusBackVO(statusBackVO);
        return Result.success().message("操作成功");
    }

    @ApiOperation(value = "查看后台菜单列表")
    @ApiImplicitParam(name = "keywords", value = "关键字(菜单名称)", dataType = "String")
    @GetMapping("/back/menus")
    public Result getBackMenus(String keywords) {
        return Result.success().message("查询成功").data(Dict.create()
                .set("homeMenuId", HOME_MENU_ID)
                .set("dataList",menuService.getMenusBackDTO(keywords)));
    }

    @ApiOperation(value = "查看用户菜单")
    @GetMapping("/back/menus/user")
    public Result getBackMenusUser() {
        return Result.success().message("查询成功").data(menuService.getMenusUserBackDTO());
    }
}
