package com.iksling.blog.exception;

public class OperationStatusException extends RuntimeException {
    public OperationStatusException() {
        super("操作状态异常!");
    }

    public OperationStatusException(String message) {
        super(message);
    }
}
