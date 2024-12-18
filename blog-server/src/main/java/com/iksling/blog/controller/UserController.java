package com.iksling.blog.controller;

import com.iksling.blog.annotation.OptLog;
import com.iksling.blog.pojo.Condition;
import com.iksling.blog.pojo.Dict;
import com.iksling.blog.pojo.Result;
import com.iksling.blog.service.UserService;
import com.iksling.blog.util.LocaleUtil;
import com.iksling.blog.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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
        return Result.success().message(LocaleUtil.getMessage("C0001"));
    }

    @OptLog(optType = SAVE)
    @ApiOperation(value = "上传用户头像")
    @ApiImplicitParam(name = "userAvatarBackVO", value = "用户头像后台VO", required = true, dataType = "UserAvatarBackVO")
    @PostMapping("/back/user/avatar")
    public Result saveBackUserAvatar(@Valid UserAvatarBackVO userAvatarBackVO) {
        return Result.success().message(LocaleUtil.getMessage("C0002")).data(userService.saveUserAvatarBackVO(userAvatarBackVO));
    }

    @OptLog(optType = REMOVE)
    @ApiOperation(value = "物理批量删除用户")
    @ApiImplicitParam(name = "idList", value = "idList", required = true, dataType = "List<Integer>")
    @DeleteMapping("/back/users")
    public Result deleteBackUsers(@RequestBody List<Integer> idList) {
        userService.deleteBackUserByIdList(idList);
        return Result.success().message(LocaleUtil.getMessage("C0001"));
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "批量更新用户状态")
    @ApiImplicitParam(name = "statusBackVO", value = "状态后台VO", required = true, dataType = "StatusBackVO")
    @PutMapping("/back/users/status")
    public Result updateBackUsersStatus(@Valid @RequestBody StatusBackVO statusBackVO) {
        userService.updateUsersStatusBackVO(statusBackVO);
        return Result.success().message(LocaleUtil.getMessage("C0001"));
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "批量更新用户头像状态")
    @ApiImplicitParam(name = "fileNameList", value = "文件名list", required = true, dataType = "List<Long>")
    @PutMapping("/back/user/avatars")
    public Result updateBackUserAvatars(@RequestBody List<Long> fileNameList) {
        userService.updateBackUserAvatarsByFileNameList(fileNameList);
        return Result.success().message(LocaleUtil.getMessage("C0001"));
    }

    @ApiOperation(value = "查看后台用户列表")
    @ApiImplicitParam(name = "condition", value = "查询条件", required = true, dataType = "Condition")
    @GetMapping("/back/users")
    public Result getBackUsers(@Valid Condition condition) {
        return Result.success().message(LocaleUtil.getMessage("C0003")).data(Dict.create()
                .set("rootUserIdList", ROOT_USER_ID_LIST)
                .set("pagePojo", userService.getUsersBackDTO(condition)));
    }

    @ApiOperation(value = "查看邮箱是否存在")
    @ApiImplicitParam(name = "keywords", value = "关键字(邮箱号)", required = true, dataType = "String")
    @GetMapping("/back/user/email")
    public Result getBackUserEmail(String keywords) {
        return Result.success().message(LocaleUtil.getMessage("C0003")).data(userService.getBackUserExistFlag(keywords, null));
    }

    @ApiOperation(value = "查看用户是否存在")
    @ApiImplicitParam(name = "keywords", value = "关键字(用户名)", required = true, dataType = "String")
    @GetMapping("/back/user/username")
    public Result getBackUserUsername(String keywords) {
        return Result.success().message(LocaleUtil.getMessage("C0003")).data(userService.getBackUserExistFlag(null, keywords));
    }

    @OptLog(optType = REMOVE)
    @ApiOperation(value = "批量下线用户")
    @ApiImplicitParam(name = "idList", value = "idList", required = true, dataType = "List<Integer>")
    @DeleteMapping("/back/user/onlines")
    public Result deleteBackUserOnlines(@RequestBody List<Integer> idList) {
        userService.deleteBackUserOnlinesByIdList(idList);
        return Result.success().message(LocaleUtil.getMessage("C0001"));
    }

    @ApiOperation(value = "查看后台在线用户列表")
    @ApiImplicitParam(name = "condition", value = "查询条件", required = true, dataType = "Condition")
    @GetMapping("/back/user/onlines")
    public Result getBackUserOnlines(@Valid Condition condition) {
        return Result.success().message(LocaleUtil.getMessage("C0003")).data(Dict.create()
                .set("rootUserIdList", ROOT_USER_ID_LIST)
                .set("pagePojo", userService.getUserOnlinesBackDTO(condition)));
    }

    /****************************************************************************************************/

    @OptLog(optType = SAVE)
    @ApiOperation(value = "上传用户头像")
    @ApiImplicitParam(name = "multiFileVO", value = "文件VO", required = true, dataType = "MultiFileVO")
    @PostMapping("/user/avatar")
    public Result saveUserAvatar(@Valid MultiFileVO multiFileVO) {
        return Result.success().message(LocaleUtil.getMessage("C0002")).data(userService.saveUserAvatar(multiFileVO));
    }

    @OptLog(optType = SAVE)
    @ApiOperation(value = "发送邮箱验证码")
    @ApiImplicitParam(name = "emailCodeVO", value = "邮箱验证码VO", required = true, dataType = "EmailCodeVO")
    @PostMapping("/user/email/code")
    public Result saveUserEmailCode(@Valid @RequestBody EmailCodeVO emailCodeVO) {
        userService.saveUserEmailCode(emailCodeVO);
        return Result.success().message(LocaleUtil.getMessage("C0004"));
    }

    @OptLog(optType = SAVE)
    @ApiOperation(value = "用户注册")
    @ApiImplicitParam(name = "userRegisterVO", value = "用户注册VO", required = true, dataType = "UserRegisterVO")
    @PostMapping("/user/register")
    public Result saveUserRegister(@Valid @RequestBody UserRegisterVO userRegisterVO) {
        userService.saveUserRegisterVO(userRegisterVO);
        return Result.success().message(LocaleUtil.getMessage("C0006"));
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "修改用户信息")
    @ApiImplicitParam(name = "userVO", value = "用户VO", required = true, dataType = "UserVO")
    @PutMapping("/user")
    public Result updateUser(@Valid @RequestBody UserVO userVO) {
        userService.updateUserVO(userVO);
        return Result.success().message(LocaleUtil.getMessage("C0001"));
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "邮箱换绑")
    @ApiImplicitParam(name = "emailVO", value = "邮箱VO", required = true, dataType = "EmailVO")
    @PutMapping("/user/email")
    public Result updateUserEmail(@Valid @RequestBody EmailVO emailVO) {
        userService.updateUserEmailVO(emailVO);
        return Result.success().message(LocaleUtil.getMessage("C0001"));
    }

    @ApiOperation(value = "qq登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "openId", value = "openId", required = true, dataType = "String"),
            @ApiImplicitParam(name = "accessToken", value = "accessToken", required = true, dataType = "String")
    })
    @PostMapping("/user/oauth/qq")
    public Result qqLogin(@Valid @RequestBody QQOauthVO qqOauthVO) {
        return Result.success().message(LocaleUtil.getMessage("C0007")).data(userService.qqLogin(qqOauthVO));
    }
}
