package com.iksling.blog.exception;

import com.iksling.blog.util.LocaleUtil;

public class AuthenticationStatusException extends RuntimeException {
    public AuthenticationStatusException() {
        super(LocaleUtil.getMessage("E0001"));
    }

    public AuthenticationStatusException(String message) {
        super(message);
    }
}
