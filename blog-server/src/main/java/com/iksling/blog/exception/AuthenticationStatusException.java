package com.iksling.blog.exception;

public class AuthenticationStatusException extends RuntimeException {
    public AuthenticationStatusException() {
        super("认证状态异常!");
    }

    public AuthenticationStatusException(String message) {
        super(message);
    }
}
