package com.iksling.blog.exception;

import com.iksling.blog.util.LocaleUtil;

public class OperationStatusException extends RuntimeException {
    public OperationStatusException() {
        super(LocaleUtil.getMessage("E0006"));
    }

    public OperationStatusException(String message) {
        super(message);
    }
}
