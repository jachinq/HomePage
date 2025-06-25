package com.home.homepage.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CalendarUtil {
    public static final String DateTimeFormatter = "yyyy-MM-dd HH:mm:ss";
    public static final String DateFormatter = "yyyy-MM-dd";
    public static final String TimeFormatter = "yyyy-MM-dd HH:mm:ss";

    public static String format(Calendar calendar, String format) {
        if (calendar == null) {
            return null;
        }
        return new SimpleDateFormat(format).format(calendar.getTime());
    }

    public static String dateTimeFormat(Calendar calendar) {
        return format(calendar, DateTimeFormatter);
    }

    public static String dateFormat(Calendar calendar) {
        return format(calendar, DateFormatter);
    }

    public static String timeFormat(Calendar calendar) {
        return format(calendar, TimeFormatter);
    }
}
