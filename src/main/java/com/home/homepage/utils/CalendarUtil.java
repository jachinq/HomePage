package com.home.homepage.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CalendarUtil {
    public static final String DateTimeFormatter = "yyyy-MM-dd HH:mm:ss";
    public static String format(Calendar calendar) {
        if (calendar == null) {
            return null;
        }
        return new SimpleDateFormat(DateTimeFormatter).format(calendar.getTime());
    }
}
