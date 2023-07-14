package com.iksling.blog.exception;

public class IllegalRequestException extends RuntimeException {
    public IllegalRequestException() {
        super("请不要瞎搞, 小心我顺着网线爬过去找你!");
    }

    public IllegalRequestException(String message) {
        super(message);
    }
}
