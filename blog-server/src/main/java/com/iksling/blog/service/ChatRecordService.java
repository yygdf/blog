package com.iksling.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iksling.blog.entity.ChatRecord;
import com.iksling.blog.pojo.Dict;
import com.iksling.blog.vo.MultiFileVO;

/**
 *
 */
public interface ChatRecordService extends IService<ChatRecord> {

    void saveChatRecord(String chatContent);

    void saveChatRecordVoice(MultiFileVO multiFileVO);

    void updateChatRecord(Integer id);

    Dict getChatRecordsDTO();
}
