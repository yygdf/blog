package com.iksling.blog.util;

import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

import static com.iksling.blog.util.CommonUtil.getSplitStringByIndex;

public class FileUploadUtil {
    public static String upload(MultipartFile file, String targetAddr, String fullFileName) {
        return FtpUtil.upload(file, targetAddr, fullFileName);
    }

    public static void rename(String path, String pathNew) {
        FtpUtil.rename(path, pathNew);
    }

    public static boolean checkNotValidAudioFileType(MultipartFile file) {
        String fileExtension = getSplitStringByIndex(Objects.requireNonNull(file.getOriginalFilename()), "\\.", -1);
        return !"wav".equalsIgnoreCase(fileExtension);
    }

    public static boolean checkNotValidImageFileType(MultipartFile file) {
        String fileExtension = getSplitStringByIndex(Objects.requireNonNull(file.getOriginalFilename()), "\\.", -1);
        return !"jpg".equalsIgnoreCase(fileExtension) && !"jpeg".equalsIgnoreCase(fileExtension) && !"png".equalsIgnoreCase(fileExtension) && !"gif".equalsIgnoreCase(fileExtension);
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
