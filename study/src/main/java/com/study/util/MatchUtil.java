package com.study.util;

import cn.hutool.core.util.ReUtil;

/**
 * 匹配工具类
 */
public class MatchUtil {
    /**
     * 是否匹配中文
     *
     * @param content
     * @return
     */
    public static boolean isMatchChinese(String content) {
        return ReUtil.isMatch(ReUtil.RE_CHINESES, content);
    }

    /**
     * 是否匹配中文
     *
     * @param content
     * @return
     */
    public static boolean isMatchChinese(char content) {
        return ReUtil.isMatch(ReUtil.RE_CHINESES, content + "");
    }

    /**
     * 匹配中文数量
     *
     * @param content
     * @return
     */
    public static int matchChineseCount(String content) {
        int count = 0;
        for (char thisChar : content.toCharArray()) {
            if (isMatchChinese(thisChar)) {
                count++;
            }
        }
        return count;
    }

}
