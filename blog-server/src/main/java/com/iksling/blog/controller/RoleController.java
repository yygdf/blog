package com.iksling.blog.controller;

import com.iksling.blog.annotation.OptLog;
import com.iksling.blog.pojo.Result;
import com.iksling.blog.service.RoleService;
import com.iksling.blog.vo.CommonStatusVO;
import com.iksling.blog.vo.RoleBackVO;
import com.iksling.blog.vo.RoleOptionVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.iksling.blog.constant.OptLogConst.*;

@RestController
@Api(tags = "角色模块")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @ApiOperation(value = "查看后台角色列表")
    @ApiImplicitParam(name = "keywords", value = "角色名称关键字", dataType = "String")
    @GetMapping("/back/roles")
    public Result listBackRoles(String keywords) {
        return Result.success().message("查询成功").data(roleService.getRolesBackDTO(keywords));
    }

    @ApiOperation(value = "查看角色选项")
    @GetMapping("/back/role/option")
    public Result listBackRoleOption() {
        return Result.success().message("查询成功").data(roleService.getRoleOptionDTO());
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "修改角色状态")
    @ApiImplicitParam(name = "commonStatusVO", value = "通用状态VO", required = true, dataType = "CommonStatusVO")
    @PutMapping("/back/role/status")
    public Result updateRoleStatus(@Valid @RequestBody CommonStatusVO commonStatusVO) {
        roleService.updateRoleStatusVO(commonStatusVO);
        return Result.success().message("操作成功");
    }

    @OptLog(optType = REMOVE)
    @ApiOperation(value = "删除角色")
    @ApiImplicitParam(name = "id", value = "角色id", required = true, dataType = "String")
    @DeleteMapping("/back/role")
    public Result deleteBackRole(@RequestBody String id) {
        roleService.deleteRoleById(id);
        return Result.success().message("操作成功");
    }

    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "添加或修改角色")
    @ApiImplicitParam(name = "roleBackVO", value = "角色后台VO", required = true, dataType = "RoleBackVO")
    @PostMapping("/back/role")
    public Result saveOrUpdateBackRole(@Valid @RequestBody RoleBackVO roleBackVO) {
        roleService.saveOrUpdateRoleBackVO(roleBackVO);
        return Result.success().message("操作成功");
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "修改角色选项")
    @ApiImplicitParam(name = "roleOptionVO", value = "角色选项VO", required = true, dataType = "RoleOptionVO")
    @PutMapping("/back/role/option")
    public Result updateBackRole(@Valid @RequestBody RoleOptionVO roleOptionVO) {
        roleService.updateRoleOptionVO(roleOptionVO);
        return Result.success().message("操作成功");
    }

    @ApiOperation(value = "查看所有的角色名")
    @GetMapping("/back/role/roleName")
    public Result listBackAllRoleName() {
        return Result.success().message("查询成功").data(roleService.getAllRoleName());
    }
}
