package com.iksling.blog.util;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

public class FileUploadUtil {
    public static String upload(MultipartFile file, String targetAddr) {
        String fileName = String.valueOf(IdWorker.getId());
        String extension = getFileExtension(Objects.requireNonNull(file.getOriginalFilename()));
        return FtpUtil.upload(file, targetAddr, fileName + extension);
    }

    public static boolean checkVoiceFileType(MultipartFile file) {
        String fileExtension = FileUploadUtil.getFileExtension(Objects.requireNonNull(file.getOriginalFilename()));
        return ".wav".equalsIgnoreCase(fileExtension);
    }

    public static boolean checkImageFileType(MultipartFile file) {
        String fileExtension = FileUploadUtil.getFileExtension(Objects.requireNonNull(file.getOriginalFilename()));
        return ".jpg".equalsIgnoreCase(fileExtension) || ".jpeg".equalsIgnoreCase(fileExtension) || ".png".equalsIgnoreCase(fileExtension) || ".gif".equalsIgnoreCase(fileExtension);
    }

    public static boolean checkFileSize(long source, int size, String unit) {
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
        return source <= size;
    }

    private static String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }
}
