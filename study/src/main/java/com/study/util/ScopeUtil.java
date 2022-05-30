package com.study.util;

import java.time.LocalDate;

/**
 * 范围工具类
 *
 * @author luolan
 */
public class ScopeUtil {

    /**
     * 两个范围是否有交集
     *
     * @param beginNum1
     * @param endNum1
     * @param beginNum2
     * @param endNum2
     * @return
     */
    public static boolean isIntersection(int beginNum1, int endNum1, int beginNum2, int endNum2) {
        return isBetween(beginNum1, beginNum2, endNum2) || isBetween(endNum1, beginNum2, endNum2)
                || isBetween(beginNum2, beginNum1, endNum1) || isBetween(endNum2, beginNum1, endNum1) ? true : false;
    }

    /**
     * 两个范围是否有交集
     *
     * @param beginDate1
     * @param endDate1
     * @param beginDate2
     * @param endDate2
     * @return
     */
    public static boolean isIntersection(LocalDate beginDate1, LocalDate endDate1, LocalDate beginDate2, LocalDate endDate2) {
        return isBetween(beginDate1, beginDate2, endDate2) || isBetween(endDate1, beginDate2, endDate2)
                || isBetween(beginDate2, beginDate1, endDate1) || isBetween(endDate2, beginDate1, endDate1) ? true : false;
    }

    /**
     * 是否介于范围内
     *
     * @param num
     * @param beginNum
     * @param endNum
     * @return
     */
    public static boolean isBetween(int num, int beginNum, int endNum) {
        return num >= beginNum && num <= endNum ? true : false;
    }

    /**
     * 是否介于范围内
     *
     * @param date
     * @param beginDate
     * @param endDate
     * @return
     */
    public static boolean isBetween(LocalDate date, LocalDate beginDate, LocalDate endDate) {
        return date.compareTo(beginDate) >= 0 && date.compareTo(endDate) <= 0 ? true : false;
    }
}
