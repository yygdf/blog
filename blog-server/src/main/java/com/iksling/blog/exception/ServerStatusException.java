package com.iksling.blog.exception;

import com.iksling.blog.util.LocaleUtil;

public class ServerStatusException extends RuntimeException {
    public ServerStatusException() {
        super(LocaleUtil.getMessage("E0007"));
    }

    public ServerStatusException(String message) {
        super(message);
    }
}
