package com.iksling.blog.exception;

import com.iksling.blog.util.LocaleUtil;

public class IllegalRequestException extends RuntimeException {
    public IllegalRequestException() {
        super(LocaleUtil.getMessage("E0004"));
    }

    public IllegalRequestException(String message) {
        super(message);
    }
}
