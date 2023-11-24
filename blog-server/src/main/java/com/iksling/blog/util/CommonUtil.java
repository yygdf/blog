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

    private byte[] H2B(String s) {
        int length = s.length();
        byte[] data = new byte[length / 2];
        for (int i = 0; i < length; i += 2)
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        return data;
    }
}
