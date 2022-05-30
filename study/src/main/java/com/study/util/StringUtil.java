package com.study.util;

import org.apache.commons.lang.StringUtils;

/**
 * @Author : luolan
 * @Date: 2022-03-18 13:25
 * @Description :
 */
public class StringUtil extends StringUtils{
    /**
     * 判断字符串是否为空
     * @param str 待判断字符串
     * @return true：空；false：非空
     */
    public static boolean isNull(String str) {
        return (str == null || "".equalsIgnoreCase(str.trim()) || "null".equalsIgnoreCase(str.trim()));
    }

    /**
     * 判断对象是否为空
     * @param obj 待判断对象
     * @return true：空；false：非空
     */
    public static boolean isNull(Object obj) {
        return (obj == null) || isNull(obj.toString());
    }

    public static String toString(Object value) {
        if (value != null) {
            return value.toString().trim();
        } else {
            return "";
        }
    }


    public void a(){

    }
}
