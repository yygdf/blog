package com.iksling.blog.controller;

import com.iksling.blog.annotation.OptLog;
import com.iksling.blog.pojo.Result;
import com.iksling.blog.service.UserService;
import com.iksling.blog.vo.ConditionVO;
import com.iksling.blog.vo.UserBackVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static com.iksling.blog.constant.OptLogConst.REMOVE;
import static com.iksling.blog.constant.OptLogConst.SAVE_OR_UPDATE;

@RestController
@Api(tags = "用户模块")
public class UserController {
    @Autowired
    private UserService userService;

    @ApiOperation(value = "查看后台用户列表")
    @ApiImplicitParam(name = "condition", value = "查询条件", required = true, dataType = "ConditionVO")
    @GetMapping("/back/users")
    public Result listBackUsers(@Valid ConditionVO condition) {
        return Result.success().message("查询成功").data(userService.getPageUsersBackDTO(condition));
    }

    @OptLog(optType = REMOVE)
    @ApiOperation(value = "批量删除用户")
    @ApiImplicitParam(name = "userIdList", value = "用户idList", required = true, dataType = "List<Integer>")
    @DeleteMapping("/back/users")
    public Result deleteBackUsers(@RequestBody List<Integer> userIdList) {
        userService.deleteUserIdList(userIdList);
        return Result.success().message("操作成功");
    }

    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "添加或修改用户")
    @ApiImplicitParam(name = "userBackVO", value = "用户后台VO", required = true, dataType = "UserBackVO")
    @PostMapping("/back/user")
    public Result saveOrUpdateBackUser(@Valid @RequestBody UserBackVO userBackVO) {
        userService.saveOrUpdateUserBackVO(userBackVO);
        return Result.success().message("操作成功");
    }

    @ApiOperation(value = "查看用户是否存在")
    @ApiImplicitParam(name = "keywords", value = "邮箱关键字", required = true, dataType = "String")
    @GetMapping("/back/user/exist")
    public Result getBackUserExistFlag(String keywords) {
        return Result.success().message("查询成功").data(userService.getBackUserExistFlag(keywords));
    }

    @ApiOperation(value = "查看后台在线用户列表")
    @ApiImplicitParam(name = "condition", value = "查询条件", required = true, dataType = "ConditionVO")
    @GetMapping("/back/user/onlines")
    public Result listBackUserOnlines(@Valid ConditionVO condition) {
        return Result.success().message("查询成功").data(userService.getPageUserOnlinesBackDTO(condition));
    }
}
