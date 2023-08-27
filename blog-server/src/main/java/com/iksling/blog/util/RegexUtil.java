package com.iksling.blog.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {
    public static boolean checkEmail(String email) {
        String rule = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
        Pattern p = Pattern.compile(rule);
        Matcher m = p.matcher(email);
        return m.matches();
    }
}
