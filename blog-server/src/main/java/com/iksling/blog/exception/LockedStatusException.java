package com.iksling.blog.exception;

import com.iksling.blog.util.LocaleUtil;

public class LockedStatusException extends AuthenticationStatusException {
    public LockedStatusException() {
        super(LocaleUtil.getMessage("E0005"));
    }

    public LockedStatusException(String message) {
        super(message);
    }
}
