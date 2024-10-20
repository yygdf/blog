package com.iksling.blog.exception;

import com.iksling.blog.util.LocaleUtil;

public class DisabledStatusException extends AuthenticationStatusException {
    public DisabledStatusException() {
        super(LocaleUtil.getMessage("E0002"));
    }

    public DisabledStatusException(String message) {
        super(message);
    }
}
