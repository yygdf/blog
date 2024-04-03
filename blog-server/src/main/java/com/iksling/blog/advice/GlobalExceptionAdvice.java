package com.iksling.blog.advice;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.iksling.blog.entity.QQAuth;
import com.iksling.blog.entity.UserAuth;
import com.iksling.blog.exception.*;
import com.iksling.blog.mapper.QQAuthMapper;
import com.iksling.blog.mapper.UserAuthMapper;
import com.iksling.blog.pojo.LoginUser;
import com.iksling.blog.pojo.Result;
import com.iksling.blog.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
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
    private RedisTemplate redisTemplate;

    @Autowired
    private UserAuthMapper userAuthMapper;
    @Autowired
    private QQAuthMapper qqAuthMapper;

    /********** 非法请求异常 **********/
    @ExceptionHandler(value = IllegalRequestException.class)
    public Result exceptionAdvice(IllegalRequestException e) {
        LoginUser loginUser = UserUtil.getLoginUser();
        BoundValueOperations<String, Integer> boundValueOperations = redisTemplate.boundValueOps(USER_ILLEGAL_OPERATION + "_" + loginUser.getUserId());
        Integer count = boundValueOperations.get();
        if (count == null || count < 3) {
            boundValueOperations.increment(1);
            boundValueOperations.expire(1, TimeUnit.DAYS);
        } else {
            userAuthMapper.update(null, new LambdaUpdateWrapper<UserAuth>()
                    .set(UserAuth::getLockedFlag, true)
                    .set(UserAuth::getDisabledFlag, true)
                    .eq(UserAuth::getUserId, loginUser.getUserId()));
            qqAuthMapper.update(null, new LambdaUpdateWrapper<QQAuth>()
                    .set(QQAuth::getLockedFlag, true)
                    .set(QQAuth::getDisabledFlag, true)
                    .eq(QQAuth::getUserId, loginUser.getUserId()));
            return Result.failure().code(ACCOUNT_LOCKED).message("账号[" + loginUser.getUsername() + "]已被锁定, 如有疑问请联系管理员[QQ: " + ADMIN_CONTACT_QQ + "]");
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
        return Result.failure().code(FAILURE).message("服务器繁忙, 如有疑问请联系管理员[QQ: " + ADMIN_CONTACT_QQ + "]");
    }
}
