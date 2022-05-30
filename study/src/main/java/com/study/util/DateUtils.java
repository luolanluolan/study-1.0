package com.study.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @Author : luolan
 * @Date: 2022-03-29 14:21
 * @Description :
 */
public class DateUtils {

    private static DateFormat ddFormat = new SimpleDateFormat("yyyy-MM-dd");

    private static DateFormat ddChFormat = new SimpleDateFormat("yyyy年M月d日");

    private static DateFormat ddShortFormat = new SimpleDateFormat("yyyyMMdd");

    private static DateFormat ssFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static DateFormat df2 = new SimpleDateFormat("yyyyMMddHHmmssSSSS");


    /*======================================Date类型转String类型==========================================*/
    /**
     * @param date
     * @return 返回 yyyy-MM-dd 格式
     */
    public static String dateToDdString(Date date) {
        if (date == null) {
            return "";
        }
        return ddFormat.format(date);
    }

    /**
     * @param date
     * @return 返回 yyyy年M月d日 格式
     */
    public static String dateToDdChString(Date date) {
        if (date == null) {
            return null;
        }
        return ddChFormat.format(date);
    }

    /**
     * @param date
     * @return 返回 yyyyMMdd 格式
     */
    public static String dateToDdShortString(Date date) {
        if (date == null) {
            return null;
        }
        return ddShortFormat.format(date);
    }

    /**
     * @param date
     * @return 返回 yyyy-MM-dd HH:mm:ss 格式
     */
    public static String dateToSsFormatString(Date date) {
        if (date == null) {
            return null;
        }
        return ssFormat.format(date);
    }

    /**
     * @param date
     * @return 返回 yyyyMMddHHmmssSSSS 格式
     */
    public static String dateToDf2FormatString(Date date) {
        if (date == null) {
            return null;
        }
        return df2.format(date);
    }

    /*======================================String类型转Date类型==========================================*/

    /**
     * @param yyyy-MM-dd格式的字符串
     */
    public static Date ddStringToDate(String dateStr) {
        Date date = null;
        try {
            date = ddFormat.parse(dateStr);
        } catch (ParseException pe) {
            return null;
        }
        return date;
    }

    /**
     * @param yyyy年M月d日 格式的字符串
     */
    public static Date ddChStringToDate(String dateStr) {
        Date date = null;
        try {
            date = ddChFormat.parse(dateStr);
        } catch (ParseException pe) {
            return null;
        }
        return date;
    }

    /**
     * @param yyyyMMDd 格式的字符串
     */
    public static Date ddShortStringToDate(String dateStr) {
        Date date = null;
        try {
            date = ddShortFormat.parse(dateStr);
        } catch (ParseException pe) {
            return null;
        }
        return date;
    }

    /**
     * @param yyyy-MM-dd HH:mm:ss 格式的字符串
     */
    public static Date ssFormatStringToDate(String dateStr) {
        Date date = null;
        try {
            date = ssFormat.parse(dateStr);
        } catch (ParseException pe) {
            return null;
        }
        return date;
    }

    /*======================================常用，当前年、月、星期几==========================================*/

    /**
     * 获取当前时间，yyyy-MM-dd格式
     * @return 当前时间
     */
    public static String getNowToDdStr() {
        Date date = new Date();
        return ddFormat.format(date);
    }

    /**
     * 获取当前时间，yyyy年M月d日格式
     * @return 当前时间
     */
    public static String getNowToDdChStr() {
        Date date = new Date();
        return ddChFormat.format(date);
    }

    /**
     * 获取当前时间，yyyy年M月d日格式
     * @return 当前时间
     */
    public static String getNowToddShortStr(String dateStr) {
        Date date = new Date();
        return ddShortFormat.format(date);
    }

    /**
     * 获取当前时间，yyyy-MM-dd HH:mm:ss格式
     * @return 当前时间
     */
    public static String getNowToSsDfStr() {
        Date date = new Date();
        return ssFormat.format(date);
    }

    /**
     * 获取当前时间，yyyyMMddHHmmss格式
     * @return 当前时间
     */
    public static String getNowToDf2Str() {
        Date date = new Date();
        return df2.format(date);
    }

    /**
     * 获取当前年份
     * @return 当前年份
     */
    public static int getCurrentYear() {
        Calendar cld = Calendar.getInstance();
        Date date = new Date();
        cld.setTime(date);
        return cld.get(Calendar.YEAR);
    }

    /**
     * 得到当前月份
     * @return 当前月份
     */
    public static int getCurrentMonth() {
        Calendar cld = Calendar.getInstance();
        Date date = new Date();
        cld.setTime(date);
        return cld.get(Calendar.MONTH);
    }

    /**
     * 得到当前日
     * @return 当前日
     */
    public static int getCurrentDay() {
        Calendar cld = Calendar.getInstance();
        Date date = new Date();
        cld.setTime(date);
        return cld.get(Calendar.DATE);
    }

    /*======================================常用==========================================*/
    /**
     * 计算从现在开始几天后的时间
     * @param afterDay 天数
     * @return 从现在开始 afterDay天后的时间
     */
    public static String getDateFromNow(int afterDay) {
        GregorianCalendar calendar = new GregorianCalendar();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + afterDay);
        Date date = calendar.getTime();
        return df.format(date);
    }

    /**
     * 比较日期，当前日期与传入日期的比较
     * @param limitDate 日期字符串
     * @return 比较的结果
     */
    public int getDateCompareForStr(Date dateCompare) {
        Calendar c = Calendar.getInstance();
        Date now = c.getTime();
        return now.compareTo(dateCompare);
    }

    /**
     * 比较日期，当前日期与传入字符串格式日期的比较
     * @param limitDate 日期字符串
     * @param formatStr 传入日期格式
     * @return 比较的结果 1、0、-1
     */
    public int getDateCompareForStr(String limitDate, String formatStr) {
        try{
            SimpleDateFormat df = new SimpleDateFormat(formatStr);
            Date dateCompare = df.parse(limitDate);
            Calendar c = Calendar.getInstance();
            Date now = c.getTime();
            return now.compareTo(dateCompare);
        }catch (ParseException pe) {
            throw new RuntimeException("传入日期转换异常，请检查日期格式！"+pe);
        }
    }

}
