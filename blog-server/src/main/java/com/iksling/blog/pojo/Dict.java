package com.iksling.blog.pojo;

import java.util.HashMap;

public class Dict extends HashMap<String, Object> {
    private Dict() {
        super(16, 0.75f);
    }

    public static Dict create() {
        return new Dict();
    }

    public Dict set(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
