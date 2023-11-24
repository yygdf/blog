package com.iksling.blog.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileTypeEnum {
    JPG("image/jpeg", "ffd8ff", new byte[]{-1, -40, -1}),
    PNG("image/png", "89504e47", new byte[]{-119, 80, 78, 71}),
    GIF("image/gif", "47494638", new byte[]{71, 73, 70, 56}),
    PDF("application/pdf", "255044462d312e", new byte[]{37, 80, 68, 70, 45, 49, 46}),
    XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "504b0304", new byte[]{80, 75, 3, 4}),
    DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document", "504b0304", new byte[]{80, 75, 3, 4}),
    PPTX("application/vnd.openxmlformats-officedocument.presentationml.presentation", "504b0304", new byte[]{80, 75, 3, 4}),
    WAV("audio/wav", "52494646", new byte[]{82, 73, 70, 70}),
    MP3("audio/mpeg", "494433", new byte[]{73, 68, 51}),
    MP4("video/mp4", "0000002066747970", new byte[]{0, 0, 0, 32, 102, 116, 121, 112}),
    AVI("video/avi", "41564920", new byte[]{65, 86, 73, 32}),
    ZIP("application/x-zip-compressed", "504b0304", new byte[]{80, 75, 3, 4}),
    RAR("application/octet-stream", "52617221", new byte[]{82, 97, 114, 33});

    /**
     * 类型
     */
    private final String contentType;

    /**
     * 魔术头
     */
    private final String magicHead;

    /**
     * 字节头
     */
    private final byte[] byteHead;
}
