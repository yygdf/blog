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

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static boolean isNotEmpty(String str) {
        return str != null && str.length() != 0;
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static boolean isNotEmpty(Map<?, ?> map) {
        return map != null && !map.isEmpty();
    }

    public static boolean isEmpty(Collection<?> coll) {
        return coll == null || coll.isEmpty();
    }

    public static boolean isNotEmpty(Collection<?> coll) {
        return coll != null && !coll.isEmpty();
    }
}
