package com.iksling.blog.exception;

public class DisabledStatusException extends AuthenticationStatusException {
    public DisabledStatusException() {
        super("账号已被禁用!");
    }

    public DisabledStatusException(String message) {
        super(message);
    }
}
