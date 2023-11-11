package com.iksling.blog.util;

import org.springframework.web.multipart.MultipartFile;

public class MultiFileUtil {
    public static String upload(MultipartFile file, String targetAddr, String fullFileName) {
        return FtpUtil.upload(file, targetAddr, fullFileName);
    }

    public static void rename(String uri, String uriNew) {
        FtpUtil.rename(uri, uriNew);
    }

    public static void copy(String from, String to) {
        FtpUtil.rename(from, to);
    }

    public static void delete(String uri) {
        FtpUtil.delete(uri);
    }

    public static boolean checkNotValidFileType(String extension, String type) {
        String fileExtension = extension.toLowerCase();
        switch (type) {
            case "IMG":
                return !"jpg".equals(fileExtension) &&
                        !"jpeg".equals(fileExtension) &&
                        !"png".equals(fileExtension) &&
                        !"gif".equals(fileExtension);
            case "AUDIO":
                return !"wav".equals(fileExtension);
        }
        return false;
    }

    public static boolean checkNotValidFileSize(long source, int size, String unit) {
        switch (unit) {
            case "KB":
                source >>= 10;
                break;
            case "MB":
                source >>= 20;
                break;
            case "GB":
                source >>= 30;
                break;
        }
        return source > size;
    }
}
