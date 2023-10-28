package com.iksling.blog.controller;

import com.iksling.blog.annotation.OptLog;
import com.iksling.blog.pojo.Dict;
import com.iksling.blog.pojo.Result;
import com.iksling.blog.service.RoleService;
import com.iksling.blog.vo.RoleBackVO;
import com.iksling.blog.vo.RolePermissionBackVO;
import com.iksling.blog.vo.StatusBackVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.iksling.blog.constant.CommonConst.ROOT_ROLE_ID;
import static com.iksling.blog.constant.LogConst.*;

@RestController
@Api(tags = "角色模块")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "添加或修改角色")
    @ApiImplicitParam(name = "roleBackVO", value = "角色后台VO", required = true, dataType = "RoleBackVO")
    @PostMapping("/back/role")
    public Result saveOrUpdateBackRole(@Valid @RequestBody RoleBackVO roleBackVO) {
        roleService.saveOrUpdateRoleBackVO(roleBackVO);
        return Result.success().message("操作成功");
    }

    @OptLog(optType = REMOVE)
    @ApiOperation(value = "物理批量删除角色")
    @ApiImplicitParam(name = "idList", value = "idList", required = true, dataType = "List<Integer>")
    @DeleteMapping("/back/roles")
    public Result deleteBackRoles(@RequestBody List<Integer> idList) {
        roleService.deleteBackRolesByIdList(idList);
        return Result.success().message("操作成功");
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "修改角色状态")
    @ApiImplicitParam(name = "statusBackVO", value = "状态后台VO", required = true, dataType = "StatusBackVO")
    @PutMapping("/back/role/status")
    public Result updateBackRoleStatus(@Valid @RequestBody StatusBackVO statusBackVO) {
        roleService.updateRoleStatusBackVO(statusBackVO);
        return Result.success().message("操作成功");
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "修改角色权限")
    @ApiImplicitParam(name = "rolePermissionBackVO", value = "角色权限后台VO", required = true, dataType = "RolePermissionBackVO")
    @PutMapping("/back/role/permission")
    public Result updateBackRolePermission(@Valid @RequestBody RolePermissionBackVO rolePermissionBackVO) {
        roleService.updateRolePermissionBackVO(rolePermissionBackVO);
        return Result.success().message("操作成功");
    }

    @ApiOperation(value = "查看后台角色列表")
    @ApiImplicitParam(name = "keywords", value = "关键字(角色名称)", dataType = "String")
    @GetMapping("/back/roles")
    public Result getBackRoles(String keywords) {
        return Result.success().message("查询成功").data(Dict.create()
                .set("rootRoleId", ROOT_ROLE_ID)
                .set("dataList", roleService.getRolesBackDTO(keywords)));
    }

    @ApiOperation(value = "查看所有的角色名")
    @GetMapping("/back/role/roleNames")
    public Result getBackRoleNames() {
        return Result.success().message("查询成功").data(roleService.getBackRoleNames());
    }

    @ApiOperation(value = "查看角色权限")
    @GetMapping("/back/role/permission")
    public Result getBackRolePermission() {
        return Result.success().message("查询成功").data(roleService.getRolePermissionBackDTO());
    }
}
