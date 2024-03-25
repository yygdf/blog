package com.iksling.blog.exception;

public class LockedStatusException extends AuthenticationStatusException {
    public LockedStatusException() {
        super("账号已被锁定!");
    }

    public LockedStatusException(String message) {
        super(message);
    }
}
