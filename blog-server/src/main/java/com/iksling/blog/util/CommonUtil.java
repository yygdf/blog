package com.iksling.blog.util;

public class CommonUtil {
    public static String getSplitStringByIndex(String str, String spl, int idx) {
        String[] strArr = str.split(spl);
        if ((idx < 0 && Math.abs(idx + 1) >= strArr.length) || idx >= strArr.length)
            return str;
        return strArr[(strArr.length + idx) % strArr.length];
    }
}
