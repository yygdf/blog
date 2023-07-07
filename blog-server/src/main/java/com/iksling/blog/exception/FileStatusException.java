package com.iksling.blog.exception;

public class FileStatusException extends RuntimeException {
    public FileStatusException() {
        super("文件状态异常!");
    }

    public FileStatusException(String message) {
        super(message);
    }
}
