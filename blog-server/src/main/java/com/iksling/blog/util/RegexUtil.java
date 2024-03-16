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

    public static String deleteHTMLTag(String source) {
        return source == null ? "" : source.replaceAll("&.{2,6}?;", "")
                .replaceAll("(?!<(img).*?>)<.*?>", "");
    }

    private static String deleteScriptTag(String source) {
        return source.replaceAll("<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>", "");
    }

    private static String deleteStyleTag(String source) {
        return source.replaceAll("<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>", "");
    }
}
