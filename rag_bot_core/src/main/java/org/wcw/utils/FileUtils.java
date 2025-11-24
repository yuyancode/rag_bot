package org.wcw.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * 文件工具类
 */
public class FileUtils {
    public static double getFileSizeInMB(MultipartFile file) {
        if (file == null) {
            return 0;
        }
        long fileSizeInBytes = file.getSize();
        // 将字节数转换为兆字节，1MB = 1024 * 1024 字节
        return (double) fileSizeInBytes / (1024 * 1024);
    }


    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }

    public static String getTypeByFileName(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
