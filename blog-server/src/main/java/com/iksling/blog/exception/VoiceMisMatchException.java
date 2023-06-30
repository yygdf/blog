package com.iksling.blog.exception;

public class VoiceMisMatchException extends FileStatusException {
    public VoiceMisMatchException(String message) {
        super("需要的文件类型: .wav  提供的类型文件类型: " + message);
    }
}
