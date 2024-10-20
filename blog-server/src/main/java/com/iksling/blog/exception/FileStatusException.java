package com.iksling.blog.exception;

import com.iksling.blog.util.LocaleUtil;

public class FileStatusException extends RuntimeException {
    public FileStatusException() {
        super(LocaleUtil.getMessage("E0003"));
    }

    public FileStatusException(String message) {
        super(message);
    }
}
