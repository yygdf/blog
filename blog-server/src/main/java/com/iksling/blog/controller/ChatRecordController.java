package com.iksling.blog.controller;

import com.iksling.blog.annotation.OptLog;
import com.iksling.blog.pojo.Result;
import com.iksling.blog.service.ChatRecordService;
import com.iksling.blog.vo.MultiFileVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.iksling.blog.constant.LogConst.SAVE;
import static com.iksling.blog.constant.LogConst.UPDATE;

@RestController
@Api(tags = "聊天室模块")
public class ChatRecordController {
    @Autowired
    private ChatRecordService chatRecordService;

    @OptLog(optType = SAVE)
    @ApiOperation(value = "发送文本消息")
    @ApiImplicitParam(name = "chatContent", value = "聊天内容", required = true, dataType = "String")
    @PostMapping("/chatRecord")
    public Result saveChatRecord(@RequestBody String chatContent) {
        chatRecordService.saveChatRecord(chatContent);
        return Result.success().message("发送成功");
    }

    @OptLog(optType = SAVE)
    @ApiOperation(value = "发送语音消息")
    @ApiImplicitParam(name = "multiFileVO", value = "文件VO", required = true, dataType = "MultiFileVO")
    @PostMapping("/chatRecord/voice")
    public Result saveChatRecordVoice(@Valid MultiFileVO multiFileVO) {
        chatRecordService.saveChatRecordVoice(multiFileVO);
        return Result.success().message("发送成功");
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "撤回聊天记录")
    @ApiImplicitParam(name = "id", value = "聊天记录id", required = true, dataType = "Integer")
    @PutMapping("/chatRecord/{id}")
    public Result updateChatRecord(@PathVariable Integer id) {
        chatRecordService.updateChatRecord(id);
        return Result.success().message("撤回成功");
    }

    @ApiOperation(value = "查看聊天记录")
    @GetMapping("/chatRecords")
    public Result getChatRecords() {
        return Result.success().message("查询成功").data(chatRecordService.getChatRecordsDTO());
    }
}
