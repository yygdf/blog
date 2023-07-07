package com.iksling.blog.exception;

public class IllegalRequestException extends RuntimeException {
    public IllegalRequestException() {
        super("非法请求异常!");
    }

    public IllegalRequestException(String message) {
        super(message);
    }
}
