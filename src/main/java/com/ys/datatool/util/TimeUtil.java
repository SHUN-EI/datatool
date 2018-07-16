package com.ys.datatool.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mo on @date  2017/3/11.
 */
public class TimeUtil {

    public static final String SQL_DATE_FORMAT_PATTERN = "yyyy/MM/dd";

    public static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd HH-mm-ss";//24小时

    public static final DateFormat SQL_DATE_FORMAT = new SimpleDateFormat(SQL_DATE_FORMAT_PATTERN);


    public static String format2SqlDateTime(Date date) {
        if (date != null) {
            return SQL_DATE_FORMAT.format(date);
        }
        return "";
    }
}
