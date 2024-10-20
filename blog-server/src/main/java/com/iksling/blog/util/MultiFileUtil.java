package com.iksling.blog.util;

import com.iksling.blog.enums.FileDirEnum;
import com.iksling.blog.enums.FileTypeEnum;
import com.iksling.blog.exception.FileStatusException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.iksling.blog.constant.CommonConst.ENABLE_FILE_TYPE_STRICT;
import static com.iksling.blog.enums.FileTypeEnum.*;

public class MultiFileUtil {
    private static byte[][] fileDirEnum1 = new byte[][] {JPG.getByteHead(), PNG.getByteHead(), GIF.getByteHead()};
    private static byte[][] fileDirEnum2 = new byte[][] {JPG.getByteHead(), PNG.getByteHead(), GIF.getByteHead(), AVI.getByteHead(), MP4.getByteHead()};
    private static byte[][] fileDirEnum3 = new byte[][] {JPG.getByteHead(), PNG.getByteHead(), GIF.getByteHead(), AVI.getByteHead(), MP4.getByteHead(), PDF.getByteHead(), XLSX.getByteHead(), DOCX.getByteHead(), PPTX.getByteHead(), WAV.getByteHead(), MP3.getByteHead(), ZIP.getByteHead(), RAR.getByteHead()};

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

    public static void checkValidFile(List<MultipartFile> fileList, FileDirEnum fileDirEnum, boolean checkSizeFlag) {
        fileList.forEach(e -> checkValidFile(e, fileDirEnum, checkSizeFlag));
    }

    public static void checkValidFile(MultipartFile file, FileDirEnum fileDirEnum, boolean checkSizeFlag) {
        if (file.isEmpty())
            throw new FileStatusException(LocaleUtil.getMessage("U0001"));
        String originalFilename = file.getOriginalFilename();
        if (CommonUtil.isEmpty(originalFilename) || originalFilename.lastIndexOf(".") == -1)
            throw new FileStatusException(LocaleUtil.getMessage("U0002"));
        String contentType = file.getContentType();
        byte[] fileByteArr;
        try {
            fileByteArr = file.getBytes();
        } catch (IOException e) {
            throw new FileStatusException(LocaleUtil.getMessage("U0003"));
        }
        switch (fileDirEnum) {
            case IMAGE_AVATAR:
            case IMAGE_ARTICLE:
                if (JPG.getContentType().equals(contentType))
                    if (ENABLE_FILE_TYPE_STRICT) {
                        checkValidFileHead(fileByteArr, JPG.getByteHead());
                    } else {
                        checkValidFileHead(fileByteArr, fileDirEnum1);
                    }
                else if (PNG.getContentType().equals(contentType))
                    if (ENABLE_FILE_TYPE_STRICT) {
                        checkValidFileHead(fileByteArr, PNG.getByteHead());
                    } else {
                        checkValidFileHead(fileByteArr, fileDirEnum1);
                    }
                else if (GIF.getContentType().equals(contentType))
                    if (ENABLE_FILE_TYPE_STRICT) {
                        checkValidFileHead(fileByteArr, GIF.getByteHead());
                    } else {
                        checkValidFileHead(fileByteArr, fileDirEnum1);
                    }
                else
                    throw new FileStatusException(LocaleUtil.getMessage("U0004"));
                break;
            case IMAGE_ALBUM:
                if (JPG.getContentType().equals(contentType))
                    if (ENABLE_FILE_TYPE_STRICT) {
                        checkValidFileHead(fileByteArr, JPG.getByteHead());
                    } else {
                        checkValidFileHead(fileByteArr, fileDirEnum2);
                    }
                else if (PNG.getContentType().equals(contentType))
                    if (ENABLE_FILE_TYPE_STRICT) {
                        checkValidFileHead(fileByteArr, PNG.getByteHead());
                    } else {
                        checkValidFileHead(fileByteArr, fileDirEnum2);
                    }
                else if (GIF.getContentType().equals(contentType))
                    if (ENABLE_FILE_TYPE_STRICT) {
                        checkValidFileHead(fileByteArr, GIF.getByteHead());
                    } else {
                        checkValidFileHead(fileByteArr, fileDirEnum2);
                    }
                else if (AVI.getContentType().equals(contentType))
                    if (ENABLE_FILE_TYPE_STRICT) {
                        checkValidFileHead(fileByteArr, AVI.getByteHead());
                    } else {
                        checkValidFileHead(fileByteArr, fileDirEnum2);
                    }
                else if (MP4.getContentType().equals(contentType))
                    if (ENABLE_FILE_TYPE_STRICT) {
                        checkValidFileHead(fileByteArr, MP4.getByteHead());
                    } else {
                        checkValidFileHead(fileByteArr, fileDirEnum2);
                    }
                else
                    throw new FileStatusException(LocaleUtil.getMessage("U0004"));
                break;
            case AUDIO_CHAT:
                if (WAV.getContentType().equals(contentType))
                    checkValidFileHead(fileByteArr, WAV.getByteHead());
                else
                    throw new FileStatusException(LocaleUtil.getMessage("U0004"));
                break;
            case AUDIO_MUSIC:
                if (MP3.getContentType().equals(contentType))
                    checkValidFileHead(fileByteArr, MP3.getByteHead());
                else
                    throw new FileStatusException(LocaleUtil.getMessage("U0004"));
                break;
            default:
                boolean flag = true;
                FileTypeEnum[] fileTypeEnumArr = FileTypeEnum.values();
                for (FileTypeEnum e : fileTypeEnumArr) {
                    if (e.getContentType().equals(contentType)) {
                        if (ENABLE_FILE_TYPE_STRICT) {
                            checkValidFileHead(fileByteArr, e.getByteHead());
                        } else {
                            checkValidFileHead(fileByteArr, fileDirEnum3);
                        }
                        flag = false;
                        break;
                    }
                }
                if (flag)
                    throw new FileStatusException(LocaleUtil.getMessage("U0004"));
        }
        if (checkSizeFlag && checkNotValidFileSize(file.getSize(), fileDirEnum.getSize(), fileDirEnum.getUnit()))
            throw new FileStatusException(LocaleUtil.getMessage("U0005"));
    }

    private static void checkValidFileHead(byte[] data, byte[] fileHead) {
        for (int i = 0; i < fileHead.length; i++) {
            if (data[i] != fileHead[i])
                throw new FileStatusException(LocaleUtil.getMessage("U0006"));
        }
    }

    private static void checkValidFileHead(byte[] data, byte[][] fileHeadArr) {
        for (byte[] fileHead : fileHeadArr) {
            boolean flag = true;
            for (int i = 0; i < fileHead.length; i++) {
                if (data[i] != fileHead[i]) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                return;
            }
        }
        throw new FileStatusException(LocaleUtil.getMessage("U0006"));
    }
}
