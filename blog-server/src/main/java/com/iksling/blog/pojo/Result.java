package com.iksling.blog.pojo;

import com.iksling.blog.constant.StatusConst;
import lombok.Getter;

@Getter
public class Result {
    private boolean flag;
    private int code;
    private String message;
    private Object data;

    private Result() {}

    public static Result success() {
        Result result = new Result();
        result.flag = true;
        result.code = StatusConst.SUCCESS;
        return result;
    }

    public static Result failure() {
        Result result = new Result();
        result.flag = false;
        return result;
    }

    public Result code(int code) {
        this.code = code;
        return this;
    }

    public Result message(String message) {
        this.message = message;
        return this;
    }

    public Result data(Object data) {
        this.data = data;
        return this;
    }
}
