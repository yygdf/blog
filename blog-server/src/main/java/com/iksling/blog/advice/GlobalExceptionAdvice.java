package com.iksling.blog.advice;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.iksling.blog.entity.QQAuth;
import com.iksling.blog.entity.UserAuth;
import com.iksling.blog.exception.*;
import com.iksling.blog.mapper.QQAuthMapper;
import com.iksling.blog.mapper.UserAuthMapper;
import com.iksling.blog.pojo.LoginUser;
import com.iksling.blog.pojo.Result;
import com.iksling.blog.util.LocaleUtil;
import com.iksling.blog.util.RedisUtil;
import com.iksling.blog.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.iksling.blog.constant.CommonConst.ADMIN_CONTACT_QQ;
import static com.iksling.blog.constant.RedisConst.USER_ILLEGAL_OPERATION;
import static com.iksling.blog.constant.StatusConst.*;

@RestControllerAdvice
public class GlobalExceptionAdvice {
    @Autowired
    private UserAuthMapper userAuthMapper;
    @Autowired
    private QQAuthMapper qqAuthMapper;

    /********** 非法请求异常 **********/
    @ExceptionHandler(value = IllegalRequestException.class)
    public Result exceptionAdvice(IllegalRequestException e) {
        LoginUser loginUser = UserUtil.getLoginUser();
        String key = USER_ILLEGAL_OPERATION + "_" + loginUser.getUserId();
        Integer count = RedisUtil.getValue(key);
        if (count == null || count < 3) {
            RedisUtil.increment(key, 1);
            RedisUtil.expire(key, 1, TimeUnit.DAYS);
        } else {
            userAuthMapper.update(null, new LambdaUpdateWrapper<UserAuth>()
                    .set(UserAuth::getLockedFlag, true)
                    .set(UserAuth::getDisabledFlag, true)
                    .eq(UserAuth::getUserId, loginUser.getUserId()));
            qqAuthMapper.update(null, new LambdaUpdateWrapper<QQAuth>()
                    .set(QQAuth::getLockedFlag, true)
                    .set(QQAuth::getDisabledFlag, true)
                    .eq(QQAuth::getUserId, loginUser.getUserId()));
            return Result.failure().code(ACCOUNT_LOCKED).message(LocaleUtil.getMessage("A0001", loginUser.getUsername(), ADMIN_CONTACT_QQ));
        }
        return Result.failure().code(ILLEGAL_REQUEST).message(e.getMessage());
    }

    /********** 参数校验异常 **********/
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result exceptionAdvice(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(","));
        return Result.failure().code(FAILURE).message("{" + message + "}");
    }

    /********** 文件状态异常 **********/
    @ExceptionHandler(value = FileStatusException.class)
    public Result exceptionAdvice(FileStatusException e) {
        return Result.failure().code(FILE_STATUS).message(e.getMessage());
    }

    /********** 操作状态异常 **********/
    @ExceptionHandler(value = OperationStatusException.class)
    public Result exceptionAdvice(OperationStatusException e) {
        return Result.failure().code(FAILURE).message(e.getMessage());
    }

    /********** 服务状态异常 **********/
    @ExceptionHandler(value = ServerStatusException.class)
    public Result exceptionAdvice(ServerStatusException e) {
        return Result.failure().code(SERVER_STATUS).message(e.getMessage());
    }

    /********** 账号认证状态异常 **********/
    @ExceptionHandler(value = LockedStatusException.class)
    public Result exceptionAdvice(LockedStatusException e) {
        return Result.failure().code(ACCOUNT_LOCKED).message(e.getMessage());
    }

    /********** 账号认证状态异常 **********/
    @ExceptionHandler(value = DisabledStatusException.class)
    public Result exceptionAdvice(DisabledStatusException e) {
        return Result.failure().code(ACCOUNT_DISABLED).message(e.getMessage());
    }

    /********** 账号认证状态异常 **********/
    @ExceptionHandler(value = AuthenticationStatusException.class)
    public Result exceptionAdvice(AuthenticationStatusException e) {
        return Result.failure().code(AUTHENTICATION_FAILURE).message(e.getMessage());
    }

    /********** 未知异常 **********/
    @ExceptionHandler(value = Exception.class)
    public Result exceptionAdvice() {
        return Result.failure().code(FAILURE).message(LocaleUtil.getMessage("A0002", ADMIN_CONTACT_QQ));
    }
}
