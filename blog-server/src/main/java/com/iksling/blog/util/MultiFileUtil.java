package com.iksling.blog.util;

import com.iksling.blog.enums.FileDirEnum;
import com.iksling.blog.enums.FileTypeEnum;
import com.iksling.blog.exception.FileStatusException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.iksling.blog.enums.FileTypeEnum.*;

public class MultiFileUtil {
    public static String upload(MultipartFile file, String targetAddr, String fullFileName) {
        return FtpUtil.upload(file, targetAddr, fullFileName);
    }

    public static void rename(String uri, String uriNew) {
        FtpUtil.rename(uri, uriNew);
    }

    public static void copy(String from, String to) {
        FtpUtil.copy(from, to);
    }

    public static void delete(String uri) {
        FtpUtil.delete(uri);
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

    public static void checkValidFile(MultipartFile file, FileDirEnum fileDirEnum) {
        if (file.isEmpty())
            throw new FileStatusException("文件不存在!");
        String originalFilename = file.getOriginalFilename();
        if (CommonUtil.isEmpty(originalFilename) || originalFilename.lastIndexOf(".") == -1)
            throw new FileStatusException("文件名格式异常!");
        String contentType = file.getContentType();
        byte[] fileByteArr;
        try {
            fileByteArr = file.getBytes();
        } catch (IOException e) {
            throw new FileStatusException("文件IO异常!");
        }
        switch (fileDirEnum) {
            case IMAGE_AVATAR:
            case IMAGE_ARTICLE:
                if (JPG.getContentType().equals(contentType))
                    checkValidFileHead(fileByteArr, JPG.getByteHead());
                else if (PNG.getContentType().equals(contentType))
                    checkValidFileHead(fileByteArr, PNG.getByteHead());
                else if (GIF.getContentType().equals(contentType))
                    checkValidFileHead(fileByteArr, GIF.getByteHead());
                else
                    throw new FileStatusException("不支持的文件类型!");
                break;
            case IMAGE_ALBUM:
                if (JPG.getContentType().equals(contentType))
                    checkValidFileHead(fileByteArr, JPG.getByteHead());
                else if (PNG.getContentType().equals(contentType))
                    checkValidFileHead(fileByteArr, PNG.getByteHead());
                else if (GIF.getContentType().equals(contentType))
                    checkValidFileHead(fileByteArr, GIF.getByteHead());
                else if (AVI.getContentType().equals(contentType))
                    checkValidFileHead(fileByteArr, AVI.getByteHead());
                else if (MP4.getContentType().equals(contentType))
                    checkValidFileHead(fileByteArr, MP4.getByteHead());
                else
                    throw new FileStatusException("不支持的文件类型!");
                break;
            case AUDIO_CHAT:
                if (WAV.getContentType().equals(contentType))
                    checkValidFileHead(fileByteArr, WAV.getByteHead());
                else
                    throw new FileStatusException("不支持的文件类型!");
                break;
            case AUDIO_MUSIC:
                if (WAV.getContentType().equals(contentType))
                    checkValidFileHead(fileByteArr, WAV.getByteHead());
                else if (MP3.getContentType().equals(contentType))
                    checkValidFileHead(fileByteArr, MP3.getByteHead());
                else
                    throw new FileStatusException("不支持的文件类型!");
                break;
            default:
                boolean flag = true;
                FileTypeEnum[] fileTypeEnumArr = FileTypeEnum.values();
                for (FileTypeEnum e : fileTypeEnumArr) {
                    if (e.getContentType().equals(contentType)) {
                        checkValidFileHead(fileByteArr, e.getByteHead());
                        flag = false;
                        break;
                    }
                }
                if (flag)
                    throw new FileStatusException("不支持的文件类型!");
        }
        if (checkNotValidFileSize(file.getSize(), fileDirEnum.getSize(), fileDirEnum.getUnit()))
            throw new FileStatusException("文件大小超出限制!");
    }

    private static void checkValidFileHead(byte[] data, byte[] fileHead) {
        for (int i = 0; i < fileHead.length; i++) {
            if (data[i] != fileHead[i])
                throw new FileStatusException("检查文件时发生错误!");
        }
    }
}
