package com.study.util;

import java.io.File;
import java.io.IOException;

/**
 * 文件工具类
 *
 * @author luolan
 */
public class FileUtil {
    /**
     * 生成新文件
     *
     * @param file 文件
     * @throws IOException
     */
    public static void createNewFile(File file) throws IOException {
        //文件不存在
        if (file.exists() == false) {
            //文件所在目录不存在
            if (file.getParentFile().exists() == false) {
                //创建文件所在目录
                file.getParentFile().mkdirs();
            }
            //创建文件
            file.createNewFile();
        }
    }
}
