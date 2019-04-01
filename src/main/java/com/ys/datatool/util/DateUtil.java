package com.ys.datatool.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by mo on @date  2018/7/7.
 */
public class DateUtil {

    public static final String CHINA_DATE_FORMAT = "yyyy年MM月dd日";
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String TIME_FORMAT = "HH:mm:ss";
    public static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd HH-mm-ss";//24小时
    public static final String SQL_DATE_FORMAT = "yyyy/MM/dd";
    public static final String SQL_DATETIME_FORMAT = "yyyy/MM/dd HH:mm:ss";
    public static final String YYMMDD_DATE_FORMAT = "yyMMdd";
    public static final String YYYYMMDD_DATE_FORMAT= "yyyyMMdd";

    //-------------------日期自动转化------------------------------------------------

    /**
     * 自动识别日期格式
     *
     * @param date
     * @return
     */
    public static String formatDateByAuto(Date date) {
        String format = DATE_FORMAT;
        if (!(date instanceof java.sql.Date) && (date.getSeconds() > 0 || date.getMinutes() > 0 || date.getHours() > 0)) {
            format = DATETIME_FORMAT;
        }
        return formatDate(date, format);
    }

    /**
     * 把字符串自动转换化时间格式
     *
     * @param dateStr
     * @return
     */
    public static Date parseDateByAuto(String dateStr) {
        if (StringUtils.isEmpty(dateStr)) {
            return null;
        }

        String format = DATE_FORMAT;
        if (dateStr.indexOf("/") > -1) {
            format = format.replace("-", "/");
        }
        if (dateStr.indexOf(":") != -1) {
            format += " HH:mm";
        }
        //存在秒
        if (dateStr.indexOf(":") != dateStr.lastIndexOf(":")) {
            format += ":ss";
        }
        return parseDate(dateStr, format);
    }

    //-----------------判断日期-----------------------------------------------

    /**
     * 判断是不是生日
     *
     * @param birthday
     * @return
     */
    public static boolean verifyBirthDay(String birthday) {
        if (TextUtils.isEmpty(birthday)) return false;
        if (!birthday.contains("-")) return false;
        String[] arr = birthday.split("-");
        if (null == arr || arr.length != 3 || arr[0].length() != 4 || arr[1].length() != 2 || arr[2].length() != 2)
            return false;
        int year = getYear(new Date());
        int birthYear = Integer.parseInt(arr[0]);
        if (birthYear <= 1900 || birthYear > year) return false;
        String curDate = formatDate(new Date(), DATE_FORMAT);
        if (birthday.compareTo(curDate) > 0) return false;
        return isDate(birthday);
    }

    /**
     * 判断是否是日期
     *
     * @param date
     * @return
     */
    public static boolean isDate(String date) {
        //判断年月日的正则表达式，接受输入格式为2010-12-24，可接受平年闰年的日期
        String regex = "(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(date).matches();
    }

    /**
     * 判断是否是润年
     *
     * @param yearNum
     * @return
     */
    public static boolean isLeapYear(int yearNum) {
        boolean isLeep = false;
        if ((yearNum % 4 == 0) && (yearNum % 100 != 0))
            isLeep = true;
        else if (yearNum % 400 == 0)
            isLeep = true;
        else {
            isLeep = false;
        }
        return isLeep;
    }

    //------------获取当前日期与时间 --------------------------------

    /**
     * 获取当前日期
     *
     * @return
     */
    public static Date getCurrDate() {
        return parseDate(formatDate(new Date()));
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static Date getCurrDateTime() {
        return parseDate(formatDate(new Date()));
    }

    /**
     * 获取当前日期
     *
     * @return
     */
    public static String getCurrDateStr() {
        return formatDate(new Date());
    }

    /**
     * 获取当前日期时间(yyyy-MM-dd)
     *
     * @return
     */
    public static String formatCurrentDate() {
        return formatDate(new Date(), DATE_FORMAT);
    }

    public static String formatCurrentDate2() {
        return formatDate(new Date(), YYMMDD_DATE_FORMAT);
    }

    /**
     * 获取当前日期时间(yyyy-MM-dd HH:mm:ss)
     *
     * @return
     */
    public static String formatCurrentDateTime() {
        return formatDate(new Date(), DATETIME_FORMAT);
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String formatCurrDateToString(String strFormat) {
        return formatDate(new Date(), strFormat);
    }

    //-----------日期时间计算--------------------------------------------

    /**
     * 日期时间相减
     *
     * @param strDateBegin
     * @param strDateEnd
     * @param iType
     * @return
     */
    public static int getDifferentDate(String strDateBegin, String strDateEnd, int iType) {
        Calendar calBegin = Calendar.getInstance();
        calBegin.setTime(parseDate(strDateBegin, DATETIME_FORMAT));
        Calendar calEnd = Calendar.getInstance();
        calBegin.setTime(parseDate(strDateEnd, DATETIME_FORMAT));
        long lBegin = calBegin.getTimeInMillis();
        long lEnd = calEnd.getTimeInMillis();
        if (iType == Calendar.SECOND)
            return (int) ((lEnd - lBegin) / 1000L);
        if (iType == Calendar.MINUTE)
            return (int) ((lEnd - lBegin) / 60000L);
        if (iType == Calendar.HOUR)
            return (int) ((lEnd - lBegin) / 3600000L);
        if (iType == Calendar.DAY_OF_MONTH) {
            return (int) ((lEnd - lBegin) / 86400000L);
        }
        return -1;
    }

    /**
     * 添加天数或者月份或年，得到新的日期时间
     *
     * @param strDate
     * @param count
     * @param dayType
     * @return
     */
    public static String getAddDateTime(String strDate, int count, int dayType) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(parseDate(strDate));
        cal.add(dayType, count);
        SimpleDateFormat sdf = null;
        if ((dayType == Calendar.YEAR) || (dayType == Calendar.MONTH) || (dayType == Calendar.DAY_OF_MONTH))
            sdf = new SimpleDateFormat(DATE_FORMAT);
        else
            sdf = new SimpleDateFormat(DATETIME_FORMAT);
        return sdf.format(cal.getTime());
    }

    /**
     * 日期增加天数
     *
     * @param date
     * @param iCount
     * @return
     */
    public static Date getAddDate(Date date, int iCount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, iCount);
        return cal.getTime();
    }

    /**
     * 比较2个不同日期
     *
     * @param dateStr1
     * @param dateStr2
     * @return
     */
    public static int compareDate(String dateStr1, String dateStr2) {
        Date date1 = parseDate(dateStr1);
        Date date2 = parseDate(dateStr2);
        if (date1.getTime() > date2.getTime())
            return -1;
        else if (date1.getTime() < date2.getTime())
            return 1;
        else
            return 0;
    }

    public static int compareDate(Date date1, Date date2) {
        if (date1.getTime() > date2.getTime())
            return -1;
        else if (date1.getTime() < date2.getTime())
            return 1;
        else
            return 0;
    }

    /**
     * 获取最早日期
     *
     * @param dateList
     * @return
     */
    public static String getEarliestDate(List<String> dateList) {
        String earliestDate = "";

        if (dateList.size() == 1)
            earliestDate = dateList.get(0);

        if (dateList.size() > 1) {
            for (int i = 0; i < dateList.size(); i++) {
                String compareDate = dateList.get(i);

                if (i == 0) {
                    earliestDate = dateList.get(0);
                    continue;
                }

                int result = compareDate(earliestDate, compareDate);
                switch (result) {
                    case -1:
                        earliestDate = compareDate;
                        break;
                    case 1:
                        earliestDate = earliestDate;
                        break;
                    case 0:
                        earliestDate = compareDate;
                        break;
                }
            }
        }

        return earliestDate;
    }


    /**
     * 时间差
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static int getDifferentDays(Date startDate, Date endDate) {
        int days = 0;
        if (startDate.after(endDate)) {
            Date temp = startDate;
            startDate = endDate;
            endDate = temp;
        }
        days = (int) (endDate.getTime() - startDate.getTime()) / 1000 * 60 * 60 * 24;
        return days;
    }

    /**
     * 当前日期的后几天
     *
     * @param date
     * @param n
     * @return
     */
    public static Date getAfterDay(Date date, int n) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, n);
        return c.getTime();
    }

    //---------获取日期时间天数----------------------------------------------

    /**
     * 获取当前日期的月份最后一天
     *
     * @param dateStr
     * @return
     */
    public static String getMonthEnd(String dateStr) {
        //当前第一天
        Date date = parseDate(getMonthBegin(dateStr));
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, 1);
        c.add(Calendar.DAY_OF_YEAR, -1);
        return formatDate(c.getTime());
    }

    public static String getMonthEnd(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DATE, c.getActualMaximum(Calendar.DATE));
        return formatDate(c.getTime());
    }

    /**
     * 获取当前日期的月份第一天
     *
     * @param dateStr
     * @return
     */
    public static String getMonthBegin(String dateStr) {
        Date date = parseDate(dateStr);
        return formatDate(date, "yyyy-MM") + 01;
    }

    public static String getMonthBegin(Date date) {
        return formatDate(date, "yyyy-MM") + 01;
    }

    //--------------格式化日期-----------------------------------------

    /**
     * 格式化日期转换为字符串
     *
     * @param date
     * @param format
     * @return
     */
    public static String formatDate(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }

    public static String formatDate(Date date) {
        return formatDate(date, DATE_FORMAT);
    }

    public static String formateChinaDate(Date date) {
        return formatDate(date, CHINA_DATE_FORMAT);
    }

    public static String formateDateTime(Date date) {
        return formatDate(date, DATETIME_FORMAT);
    }

    public static String formatSQLDate(Date date) {
        return formatDate(date, SQL_DATE_FORMAT);
    }

    public static String formatSQLDateTime(Date date) {
        return formatDate(date, SQL_DATETIME_FORMAT);
    }


    public static String formateTime(Date date) {
        return formatDate(date, TIME_FORMAT);
    }


    /**
     * 日期转换
     * yyyy-mm-dd,yyyy-mm-d,yyyy-m-d,yyyy-m-dd,
     * yyyy-mm-dd hh:mm:ss,yyyy-mm-d hh:mm:ss,yyyy-m-d hh:mm:ss,yyyy-m-dd hh:mm:ss
     * yyyy-mm-dd hh:mm,yyyy-mm-d hh:mm,yyyy-m-d hh:mm,yyyy-m-dd hh:mm
     * yyyy/mm/dd,yyyy/mm/d,yyyy/m/d,yyyy/m/d,
     * yyyy/mm/dd hh:mm:ss,yyyy/mm/d hh:mm:ss，yyyy/m/d hh:mm:ss，yyyy/m/dd hh:mm:ss
     * yyyy/mm/dd hh:mm，yyyy/mm/d hh:mm，yyyy/m/d hh:mm，yyyy/m/dd hh:mm，
     * <p>
     * 转换为yyyy/mm/dd hh:mm:ss
     *
     * @param dateStr
     * @return
     */
    public static String formatSQLDateTime(String dateStr) {
        if ("".equals(dateStr) || StringUtils.isBlank(dateStr)) {
            return "";
        }

        if (dateStr.contains("T")) {
            dateStr = dateStr.replace("T", " ");
        }

        Date date = DateUtil.parseDateByAuto(dateStr);
        String result = DateUtil.formatSQLDateTime(date);

        return result;
    }


    /**
     * 日期转换
     * <p>
     * yyyy-mm-dd,yyyy-mm-d,yyyy-m-d,yyyy-m-dd,
     * yyyy-mm-dd hh:mm:ss,yyyy-mm-d hh:mm:ss,yyyy-m-d hh:mm:ss,yyyy-m-dd hh:mm:ss
     * yyyy-mm-dd hh:mm,yyyy-mm-d hh:mm,yyyy-m-d hh:mm,yyyy-m-dd hh:mm
     * yyyy/mm/dd,yyyy/mm/d,yyyy/m/d,yyyy/m/d,
     * yyyy/mm/dd hh:mm:ss,yyyy/mm/d hh:mm:ss，yyyy/m/d hh:mm:ss，yyyy/m/dd hh:mm:ss
     * yyyy/mm/dd hh:mm，yyyy/mm/d hh:mm，yyyy/m/d hh:mm，yyyy/m/dd hh:mm，
     * <p>
     * 转换为yyyy/mm/dd
     *
     * @param dateStr
     * @return
     */
    public static String formatSQLDate(String dateStr) {

        if ("".equals(dateStr) || StringUtils.isBlank(dateStr)) {
            return "";
        }

        // dateStr="2018-01-23T00:00:00";
        if (dateStr.contains("T")) {
            dateStr = dateStr.replace("T", " ");
        }

        Date date = DateUtil.parseDateByAuto(dateStr);
        String result = DateUtil.formatSQLDate(date);

        return result;
    }

    /**
     * 毫秒数转换为日期(yyyy/MM/dd)
     *
     * @param millisecond
     * @return
     */
    public static String formatMillisecond2DateTime(String millisecond) {
        if ("".equals(millisecond) || StringUtils.isBlank(millisecond)) {
            return "";
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT_PATTERN);
        Date date = new Date();
        date.setTime(Long.parseLong(millisecond));
        String result = simpleDateFormat.format(date);
        return result.substring(0, 10).replace("-", "/");
    }

    //-----------------格式化字符串为日期--------------------------------------

    /**
     * 格式化字符串转换为日期
     *
     * @param date
     * @param format
     * @return
     */
    public static Date parseDate(String date, String format) {
        try {
            return new SimpleDateFormat(format).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date parseDate(String date) {
        return parseDate(date, DATE_FORMAT);
    }

    public static Date parseSQLDate(String date) {
        return parseDate(date, SQL_DATE_FORMAT);
    }

    public static Date parseChinaDate(String date) {
        return parseDate(date, CHINA_DATE_FORMAT);
    }

    public static Date parseDateTime(String date) {
        return parseDate(date, DATETIME_FORMAT);
    }

    public static Date parseTime(String date) {
        return parseDate(date, TIME_FORMAT);
    }

    //---获取年月日时分秒----------------------------------------------------

    /**
     * 获取年份
     *
     * @param date
     * @return
     */
    public static int getYear(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.YEAR);
    }

    /**
     * 获取月份
     *
     * @param date
     * @return
     */
    public static int getMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取日
     *
     * @param date
     * @return
     */
    public static int getDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取星期
     *
     * @param date
     * @return
     */
    public static int getWeek(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 获取时间
     *
     * @param date
     * @return
     */
    public static int getHour(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.HOUR_OF_DAY);

    }

    /**
     * 获取分钟
     *
     * @param date
     * @return
     */
    public static int getMinute(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.MINUTE);
    }

    /**
     * 获取秒
     *
     * @param date
     * @return
     */
    public static int getSecond(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.SECOND);
    }

    //--------------获取星期几---------------------------------------------------

    /**
     * 获取星期几
     *
     * @param strDate
     * @return
     */
    public static String getWeekDayName(String strDate) {
        String[] mName = {"日", "一", "二", "三", "四", "五", "六"};
        Date date = parseDate(strDate);
        int week = getWeek(date);
        return "星期" + mName[week];
    }

    public static String getWeekDayName(Date date) {
        String[] mName = {"日", "一", "二", "三", "四", "五", "六"};
        int week = getWeek(date);
        return "星期" + mName[week];
    }

    /**
     * 获取一年中的星期几ss
     *
     * @param date
     * @return
     */
    public static int getWeekNumOfYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    public static int getWeekNumOfYear(String date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(parseDate(date, DATE_FORMAT));
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 获取本周星期一的日期
     *
     * @param yearNum
     * @param weekNum
     * @return
     */
    public static String getYearWeekFirstDay(int yearNum, int weekNum) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, yearNum);
        cal.set(Calendar.WEEK_OF_YEAR, weekNum);
        cal.set(Calendar.DAY_OF_WEEK, 2);
        String tempYear = Integer.toString(yearNum);
        String tempMonth = Integer.toString(cal.get(Calendar.MONTH) + 1);
        String tempDay = Integer.toString(cal.get(Calendar.DAY_OF_MONTH) - 1);
        return tempYear + "-" + tempMonth + "-" + tempDay;
    }

    /**
     * 获取本周星期天的日期
     *
     * @param yearNum
     * @param weekNum
     * @return
     */
    public static String getYearWeekEndDay(int yearNum, int weekNum) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, yearNum);
        cal.set(Calendar.WEEK_OF_YEAR, weekNum + 1);
        cal.set(Calendar.DAY_OF_WEEK, 1);
        String tempYear = Integer.toString(yearNum);
        String tempMonth = Integer.toString(cal.get(Calendar.MONTH) + 1);
        String tempDay = Integer.toString(cal.get(Calendar.DAY_OF_MONTH) - 1);
        return tempYear + "-" + tempMonth + "-" + tempDay;
    }

    //--------------获取天数---------------------------------------------------

    /**
     * 获取某年某月的第一天
     *
     * @param yearNum
     * @param monthNum
     * @return
     */
    public static Date getYearMonthFirstDay(int yearNum, int monthNum) {
        Calendar cal = Calendar.getInstance();
        cal.set(yearNum, monthNum - 1, 1, 0, 0, 0);
        cal.set(14, 0);
        return cal.getTime();
    }

    /**
     * 获取某年下个月的第一天
     *
     * @param yearNum
     * @param monthNum
     * @return
     */
    public static Date getNextYearMonthFirstDay(int yearNum, int monthNum) {
        Calendar cal = Calendar.getInstance();
        cal.set(yearNum, monthNum, 1, 0, 0, 0);
        cal.set(14, 0);
        return cal.getTime();
    }

    /**
     * 获取某年某月的最后一天
     *
     * @param yearNum
     * @param monthNum
     * @return
     */
    public static Date getYearMonthEndDay(int yearNum, int monthNum) {
        Calendar cal = Calendar.getInstance();
        cal.set(yearNum, monthNum, 0, 0, 0, 0);
        cal.set(14, 0);
        return cal.getTime();
    }

    /**
     * 获取某月的第一天
     *
     * @param date
     * @return
     */
    public static Date getYearMonthFirstDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(5, 1);
        cal.set(11, 0);
        cal.set(12, 0);
        cal.set(13, 0);
        cal.set(14, 0);
        return cal.getTime();
    }

    /**
     * 获取下一年的第一天
     *
     * @param date
     * @return
     */
    public static Date getNextYearMonthFirstDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(2, 1);
        cal.set(5, 1);
        cal.set(11, 0);
        cal.set(12, 0);
        cal.set(13, 0);
        cal.set(14, 0);
        return cal.getTime();
    }

    /**
     * 获取当前月的最后一天
     *
     * @param date
     * @return
     */
    public static Date getYearMonthEndDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(5, cal.getActualMaximum(5));
        cal.set(11, 0);
        cal.set(12, 0);
        cal.set(13, 0);
        cal.set(14, 0);
        return cal.getTime();
    }

    /**
     * 获取当年的第一天
     *
     * @param yearNum
     * @return
     */
    public static Date getYearFirstDay(int yearNum) {
        Calendar cal = Calendar.getInstance();
        cal.set(yearNum, 0, 1, 0, 0, 0);
        cal.set(14, 0);
        return cal.getTime();
    }

    /**
     * 获取下一年的第一天
     *
     * @param yearNum
     * @return
     */
    public static Date getNextYearFirstDay(int yearNum) {
        Calendar cal = Calendar.getInstance();
        cal.set(yearNum, 12, 1, 0, 0, 0);
        cal.set(14, 0);
        return cal.getTime();
    }

    /**
     * 获取当年的最后一天
     *
     * @param yearNum
     * @return
     */
    public static Date getYearEndDay(int yearNum) {
        Calendar cal = Calendar.getInstance();
        cal.set(yearNum, 12, 0, 0, 0, 0);
        cal.set(14, 0);
        return cal.getTime();
    }

    /**
     * 获取当前星期
     *
     * @param strDate
     * @param weekNum
     * @return
     */
    public static String getWeek(String strDate, int weekNum) {
        Calendar c = Calendar.getInstance();
        c.setTime(parseDate(strDate));
        if (weekNum == 1)
            c.set(7, 2);
        else if (weekNum == 2)
            c.set(7, 3);
        else if (weekNum == 3)
            c.set(7, 4);
        else if (weekNum == 4)
            c.set(7, 5);
        else if (weekNum == 5)
            c.set(7, 6);
        else if (weekNum == 6)
            c.set(7, 7);
        else if (weekNum == 0)
            c.set(7, 1);
        return formatDate(c.getTime());
    }

    public static Date getWeek(Date date, int weekNum) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        if (weekNum == 1)
            c.set(7, 2);
        else if (weekNum == 2)
            c.set(7, 3);
        else if (weekNum == 3)
            c.set(7, 4);
        else if (weekNum == 4)
            c.set(7, 5);
        else if (weekNum == 5)
            c.set(7, 6);
        else if (weekNum == 6)
            c.set(7, 7);
        else if (weekNum == 0)
            c.set(7, 1);
        return c.getTime();
    }

    /**
     * 获取下个月日期
     *
     * @param date
     * @return
     */
    public static Date getNextMonday(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        do
            c.add(Calendar.DAY_OF_MONTH, 1);
        while (c.get(Calendar.DAY_OF_WEEK) != 2);
        return c.getTime();
    }

    /**
     * 获取某一日期的前一天
     *
     * @param date
     * @return
     */
    public static Date getPreviousDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -1);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return getSqlDate(calendar.getTime());
    }

    /**
     * 获取某年某月最后一天的日期
     *
     * @param year
     * @param month
     * @return
     */
    public static Date getLastDayOfMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DATE, 1);
        return getPreviousDate(getSqlDate(calendar.getTime()));
    }

    /**
     * 获取一个月的天数
     *
     * @param year
     * @param month
     * @return
     */
    public static int getDaysInMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);// Java月份才0开始算
        return cal.getActualMaximum(Calendar.DATE);
    }

    //----------------根据用户生日计算年龄-------------------------------------------------

    /**
     * 根据用户生日计算年龄
     *
     * @param birthday
     * @return
     */
    public static int getAgeByBirthday(Date birthday) {
        Calendar cal = Calendar.getInstance();
        if (cal.before(birthday)) {
            throw new IllegalArgumentException("The birthDay is before Now.It's unbelievable!");
        }
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthday);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH) + 1;
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
        int age = yearNow - yearBirth;
        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;
                }
            } else {
                age--;
            }
        }
        return age;
    }

    /**
     * 由java.util.Date到java.sql.Date的类型转换
     *
     * @param date
     * @return
     */
    public static Date getSqlDate(java.util.Date date) {
        return new Date(date.getTime());
    }


    /**
     * 毫秒转换
     * @param ms
     * @return
     */
    public static String formatTime(long ms) {

        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;

        long day = ms / dd;
        long hour = (ms - day * dd) / hh;
        long minute = (ms - day * dd - hour * hh) / mi;
        long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

        StringBuffer sb = new StringBuffer();
        if(day > 0) {
            sb.append(day+"天");
        }
        if(hour > 0) {
            sb.append(hour+"小时");
        }
        if(minute > 0) {
            sb.append(minute+"分");
        }
        if(second > 0) {
            sb.append(second+"秒");
        }
        if(milliSecond > 0) {
            sb.append(milliSecond+"毫秒");
        }
        return sb.toString();
    }


    /**
     * 毫秒转换
     * @param ms
     * @return
     */
    public static String formatTime(Long ms) {
        Integer ss = 1000;
        Integer mi = ss * 60;
        Integer hh = mi * 60;
        Integer dd = hh * 24;

        Long day = ms / dd;
        Long hour = (ms - day * dd) / hh;
        Long minute = (ms - day * dd - hour * hh) / mi;
        Long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        Long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

        StringBuffer sb = new StringBuffer();
        if(day > 0) {
            sb.append(day+"天");
        }
        if(hour > 0) {
            sb.append(hour+"小时");
        }
        if(minute > 0) {
            sb.append(minute+"分");
        }
        if(second > 0) {
            sb.append(second+"秒");
        }
        if(milliSecond > 0) {
            sb.append(milliSecond+"毫秒");
        }
        return sb.toString();
    }



}
