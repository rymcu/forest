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
        StringBuilder sBuilder = new StringBuilder(s);
        for (int i = 0; i < len - sBuilder.length(); ++i) {
            sBuilder.insert(0, "0");
        }
        return sBuilder.toString();
    }

    public static String getYear(Calendar cal) {
        return String.valueOf(cal.get(Calendar.YEAR));
    }

    public static String getMonth(Calendar cal) {
        return strLen(String.valueOf(cal.get(Calendar.MONTH) + 1), 2);
    }

    public static String getDay(Calendar cal) {
        return strLen(String.valueOf(cal.get(Calendar.DATE)), 2);
    }

    public static String getNowDateNum() {
        Calendar cal = Calendar.getInstance();
        return getYear(cal) + getMonth(cal) + getDay(cal);
    }
}
