package com.study.util;

/**
 * 相等工具类
 *
 * @author luolan
 */
public class EqualsUtil {
    /**
     * Integer类型判断相等
     *
     * @param num1
     * @param num2
     * @return true相等，false不相等
     */
    public static boolean equals(Integer num1, Integer num2) {
        if (num1 == null && num2 == null) {
            return true;
        }
        if (num1 == null || num2 == null) {
            return false;
        }
        return num1.equals(num2) ? true : false;
    }
}
