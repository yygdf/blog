package com.iksling.blog.util;

import java.util.Collection;
import java.util.Map;

public class CommonUtil {
    public static String getSplitStringByIndex(String str, String spl, int idx) {
        String[] strArr = str.split(spl);
        if ((idx < 0 && Math.abs(idx + 1) >= strArr.length) || idx >= strArr.length)
            return str;
        return strArr[(strArr.length + idx) % strArr.length];
    }

    public static boolean isBlank(String str) {
        return "".equals(str);
    }

    public static boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static boolean isEmpty(Collection<?> coll) {
        return coll == null || coll.isEmpty();
    }
}
