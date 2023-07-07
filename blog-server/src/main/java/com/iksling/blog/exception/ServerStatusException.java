package com.iksling.blog.exception;

public class ServerStatusException extends RuntimeException {
    public ServerStatusException() {
        super("服务状态异常!");
    }

    public ServerStatusException(String message) {
        super(message);
    }
}
