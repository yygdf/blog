package com.iksling.blog.dto;

import com.iksling.blog.entity.ChatRecord;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRecordDTO implements Serializable {
    /**
     * 聊天记录
     */
    private List<ChatRecord> chatRecordList;

    /**
     * ip来源
     */
    private String ipSource;

    /**
     * ip地址
     */
    private String ipAddress;
}