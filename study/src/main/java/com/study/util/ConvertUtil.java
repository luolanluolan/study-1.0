package com.study.util;

import com.google.common.primitives.Bytes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 转换工具类
 *
 * @author luolan
 */
public class ConvertUtil {
    /**
     * byte数组转List
     *
     * @param bytes
     * @return
     */
    public static List<Byte> arrayToList(byte[] bytes) {
        return new ArrayList<>(Bytes.asList(bytes));
    }

    /**
     * Byte数组转List
     *
     * @param bytes
     * @return
     */
    public static List<Byte> arrayToList(Byte[] bytes) {
        return new ArrayList<>(Arrays.asList(bytes));
    }

    /**
     * Integer数组转List
     *
     * @param array
     * @return
     */
    public static List<Integer> arrayToList(Integer[] array) {
        return new ArrayList<>(Arrays.asList(array));
    }

    /**
     * Long数组转List
     *
     * @param array
     * @return
     */
    public static List<Long> arrayToList(Long[] array) {
        return new ArrayList<>(Arrays.asList(array));
    }

    /**
     * Double数组转List
     *
     * @param array
     * @return
     */
    public static List<Double> arrayToList(Double[] array) {
        return new ArrayList<>(Arrays.asList(array));
    }

    /**
     * Float数组转List
     *
     * @param array
     * @return
     */
    public static List<Float> arrayToList(Float[] array) {
        return new ArrayList<>(Arrays.asList(array));
    }
}
