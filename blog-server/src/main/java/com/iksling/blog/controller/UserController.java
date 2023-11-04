package com.iksling.blog.controller;

import com.iksling.blog.annotation.OptLog;
import com.iksling.blog.pojo.Dict;
import com.iksling.blog.pojo.Result;
import com.iksling.blog.service.UserService;
import com.iksling.blog.vo.ConditionBackVO;
import com.iksling.blog.vo.StatusBackVO;
import com.iksling.blog.vo.UserBackVO;
import com.iksling.blog.vo.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.iksling.blog.constant.CommonConst.ROOT_USER_ID_LIST;
import static com.iksling.blog.constant.LogConst.*;

@RestController
@Api(tags = "用户模块")
public class UserController {
    @Autowired
    private UserService userService;

    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "添加或修改用户")
    @ApiImplicitParam(name = "userBackVO", value = "用户后台VO", required = true, dataType = "UserBackVO")
    @PostMapping("/back/user")
    public Result saveOrUpdateBackUser(@Valid @RequestBody UserBackVO userBackVO) {
        userService.saveOrUpdateUserBackVO(userBackVO);
        return Result.success().message("操作成功");
    }

    @OptLog(optType = REMOVE)
    @ApiOperation(value = "物理批量删除用户")
    @ApiImplicitParam(name = "idList", value = "idList", required = true, dataType = "List<Integer>")
    @DeleteMapping("/back/users")
    public Result deleteBackUsers(@RequestBody List<Integer> idList) {
        userService.deleteBackUserByIdList(idList);
        return Result.success().message("操作成功");
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "批量更新用户状态")
    @ApiImplicitParam(name = "statusBackVO", value = "状态后台VO", required = true, dataType = "StatusBackVO")
    @PutMapping("/back/users/status")
    public Result updateBackUsersStatus(@Valid @RequestBody StatusBackVO statusBackVO) {
        userService.updateUsersStatusBackVO(statusBackVO);
        return Result.success().message("操作成功");
    }

    @ApiOperation(value = "查看后台用户列表")
    @ApiImplicitParam(name = "condition", value = "查询条件", required = true, dataType = "ConditionBackVO")
    @GetMapping("/back/users")
    public Result getBackUsers(@Valid ConditionBackVO condition) {
        return Result.success().message("查询成功").data(Dict.create()
                .set("rootUserIdList", ROOT_USER_ID_LIST)
                .set("pagePojo", userService.getUsersBackDTO(condition)));
    }

    @ApiOperation(value = "查看邮箱是否存在")
    @ApiImplicitParam(name = "keywords", value = "关键字(邮箱号)", required = true, dataType = "String")
    @GetMapping("/back/user/email")
    public Result getBackUserEmail(String keywords) {
        return Result.success().message("查询成功").data(userService.getBackUserExistFlag(keywords, null));
    }

    @ApiOperation(value = "查看用户是否存在")
    @ApiImplicitParam(name = "keywords", value = "关键字(用户名)", required = true, dataType = "String")
    @GetMapping("/back/user/username")
    public Result getBackUserUsername(String keywords) {
        return Result.success().message("查询成功").data(userService.getBackUserExistFlag(null, keywords));
    }

    @OptLog(optType = REMOVE)
    @ApiOperation(value = "批量下线用户")
    @ApiImplicitParam(name = "idList", value = "idList", required = true, dataType = "List<Integer>")
    @DeleteMapping("/back/user/onlines")
    public Result deleteBackUserOnlines(@RequestBody List<Integer> idList) {
        userService.deleteBackUserOnlinesByIdList(idList);
        return Result.success().message("操作成功");
    }

    @ApiOperation(value = "查看后台在线用户列表")
    @ApiImplicitParam(name = "condition", value = "查询条件", required = true, dataType = "ConditionBackVO")
    @GetMapping("/back/user/onlines")
    public Result getBackUserOnlines(@Valid ConditionBackVO condition) {
        return Result.success().message("查询成功").data(Dict.create()
                .set("rootUserIdList", ROOT_USER_ID_LIST)
                .set("pagePojo", userService.getUserOnlinesBackDTO(condition)));
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "修改用户信息")
    @ApiImplicitParam(name = "userVO", value = "用户VO", required = true, dataType = "UserVO")
    @PutMapping("/user")
    public Result updateUser(@Valid @RequestBody UserVO userVO) {
        userService.updateUserVO(userVO);
        return Result.success().message("操作成功");
    }
}
