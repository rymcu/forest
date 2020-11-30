package com.rymcu.forest.util;

import org.apache.commons.lang.StringUtils;

import java.util.Calendar;

/**
 * @author ronger
 */
public class DateUtil {

    public static String strLen(String s, int len) {
        if (StringUtils.isBlank(s)) {
            s = "";
        }
        for (int i = 0; i < len - s.length(); ++i) {
            s = "0" + s;
        }
        return s;
    }

    public static String getYear(Calendar cal) {
        return String.valueOf(cal.get(1));
    }

    public static String getMonth(Calendar cal) {
        return strLen(String.valueOf(cal.get(2) + 1), 2);
    }

    public static String getDay(Calendar cal) {
        return strLen(String.valueOf(cal.get(5)), 2);
    }

    public static String getNowDateNum() {
        Calendar cal = Calendar.getInstance();
        return getYear(cal)  + getMonth(cal)  + getDay(cal);
    }
}
