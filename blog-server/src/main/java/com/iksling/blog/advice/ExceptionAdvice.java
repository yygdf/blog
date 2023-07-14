package com.iksling.blog.advice;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.iksling.blog.constant.CommonConst;
import com.iksling.blog.entity.UserAuth;
import com.iksling.blog.exception.FileStatusException;
import com.iksling.blog.exception.IllegalRequestException;
import com.iksling.blog.exception.OperationStatusException;
import com.iksling.blog.mapper.UserAuthMapper;
import com.iksling.blog.pojo.Result;
import com.iksling.blog.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.iksling.blog.constant.RedisConst.USER_ILLEGAL_OPERATION;
import static com.iksling.blog.constant.StatusConst.*;

@RestControllerAdvice
public class ExceptionAdvice {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private UserAuthMapper userAuthMapper;

    /********** 非法请求异常 **********/
    @ExceptionHandler(value = IllegalRequestException.class)
    public Result exceptionAdvice(IllegalRequestException e) {
        Integer userId = UserUtil.getLoginUser().getUserId();
        String username = UserUtil.getLoginUser().getUsername();
        redisTemplate.boundHashOps(USER_ILLEGAL_OPERATION).expire(1, TimeUnit.DAYS);
        redisTemplate.boundHashOps(USER_ILLEGAL_OPERATION).increment(userId.toString(), 1);
        Integer count = (Integer) redisTemplate.boundHashOps(USER_ILLEGAL_OPERATION).get(userId.toString());
        if (Objects.nonNull(count) && count >= 3) {
            userAuthMapper.update(null, new LambdaUpdateWrapper<UserAuth>()
                    .set(UserAuth::getLockedFlag, true)
                    .set(UserAuth::getDisabledFlag, true)
                    .eq(UserAuth::getUserId, userId));
            return Result.failure().code(ACCOUNT_LOCKED).message("账户[" + username + "]已被锁定, 如有疑问请联系管理员[" + CommonConst.CONTACT + "]");
        }
        return Result.failure().code(ILLEGAL_REQUEST).message(e.getMessage());
    }

    /********** 参数校验异常 **********/
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result exceptionAdvice(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return Result.failure().code(FAILURE).message(message);
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

    /********** 未知异常 **********/
//    @ExceptionHandler(value = Exception.class)
    public Result exceptionAdvice(Exception e) {
        return Result.failure().code(FAILURE).message("服务器繁忙, 如有疑问请联系管理员[" + CommonConst.CONTACT + "]");
    }
}
