package com.iksling.blog.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    public static Date getSomeDay(Date date, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, day);
        return calendar.getTime();
    }

    public static Date getSomeHour(Date date, int hour) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hour);
        return calendar.getTime();
    }

    public static Date strToDate(String strDate, String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        try {
            return formatter.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String dateToStr(Date date, String pattern) {
        return new SimpleDateFormat(pattern).format(date);
    }

    public static int getDayOfWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK) - 1;
    }

    public static int getDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    public static int getDayOfYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    public static String getMinTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return getMinTime(calendar);
    }

    public static String getMaxTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return getMaxTime(calendar);
    }

    private static String getMinTime(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return dateToStr(calendar.getTime(), YYYY_MM_DD_HH_MM_SS);
    }

    private static String getMaxTime(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calendar.getActualMaximum(Calendar.MILLISECOND));
        return dateToStr(calendar.getTime(), YYYY_MM_DD_HH_MM_SS);
    }
}
