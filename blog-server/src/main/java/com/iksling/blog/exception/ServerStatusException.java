package com.iksling.blog.exception;

public class ServerStatusException extends RuntimeException {
    public ServerStatusException(String message) {
        super(message);
    }
}
