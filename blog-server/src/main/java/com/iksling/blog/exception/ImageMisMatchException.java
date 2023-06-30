package com.iksling.blog.exception;

public class ImageMisMatchException extends FileStatusException {
    public ImageMisMatchException(String message) {
        super("需要的文件类型: .jpg .jpeg .png .gif  提供的类型文件类型: " + message);
    }
}
