package com.iksling.blog.advice;

import com.iksling.blog.constant.CommonConst;
import com.iksling.blog.exception.FileStatusException;
import com.iksling.blog.exception.IllegalRequestException;
import com.iksling.blog.exception.OperationStatusException;
import com.iksling.blog.pojo.Result;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

import static com.iksling.blog.constant.StatusConst.*;

@RestControllerAdvice
public class ExceptionAdvice {
    /********** 非法请求异常 **********/
    @ExceptionHandler(value = IllegalRequestException.class)
    public Result exceptionAdvice(IllegalRequestException e) {
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

    /********** 未知异常 **********/
//    @ExceptionHandler(value = Exception.class)
    public Result exceptionAdvice(Exception e) {
        return Result.failure().code(FAILURE).message("发生未知异常,请联系管理员[" + CommonConst.CONTACT + "]");
    }

    /********** 操作状态异常 **********/
    @ExceptionHandler(value = OperationStatusException.class)
    public Result exceptionAdvice(OperationStatusException e) {
        return Result.failure().code(FAILURE).message(e.getMessage());
    }
}
