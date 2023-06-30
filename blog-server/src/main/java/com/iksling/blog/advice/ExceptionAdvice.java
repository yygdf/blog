package com.iksling.blog.advice;

import com.iksling.blog.exception.IllegalRequestException;
import com.iksling.blog.pojo.Result;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

import static com.iksling.blog.constant.StatusConst.FAILURE;
import static com.iksling.blog.constant.StatusConst.ILLEGAL_REQUEST;

@RestControllerAdvice
public class ExceptionAdvice {
    /********** 非法请求异常 **********/
    @ExceptionHandler(value = IllegalRequestException.class)
    public Result exceptionAdvice(IllegalRequestException e) {
        return Result.failure().code(ILLEGAL_REQUEST).message(e.getMessage());
    }

    /********** 参数校验异常 **********/
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result errorHandler(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return Result.failure().code(FAILURE).message(message);
    }
}
