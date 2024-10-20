package com.iksling.blog.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class LocaleUtil {
    private static MessageSource messageSource;

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        LocaleUtil.messageSource = messageSource;
    }

    public static String getMessage(String key, Object... args) {
        try {
            return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
        } catch (NoSuchMessageException e) {
            return key;
        }
    }

    public static String getMessageByLang(String key, String lang, Object... args) {
        try {
            Locale locale = Locale.getDefault();
            if (lang != null) {
                if (lang.contains("_")) {
                    String[] localeMessage = lang.split("_");
                    locale = new Locale(localeMessage[0], localeMessage[1]);
                } else
                    locale = new Locale(lang);
            }
            return messageSource.getMessage(key, args, locale);
        } catch (NoSuchMessageException e) {
            return key;
        }
    }
}
