package com.iksling.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iksling.blog.dto.ChatRecordsDTO;
import com.iksling.blog.entity.ChatRecord;
import com.iksling.blog.entity.MultiFile;
import com.iksling.blog.entity.User;
import com.iksling.blog.exception.FileStatusException;
import com.iksling.blog.exception.OperationStatusException;
import com.iksling.blog.listener.WebSocketListener;
import com.iksling.blog.mapper.ChatRecordMapper;
import com.iksling.blog.mapper.MultiFileMapper;
import com.iksling.blog.mapper.UserMapper;
import com.iksling.blog.pojo.Dict;
import com.iksling.blog.pojo.LoginUser;
import com.iksling.blog.service.ChatRecordService;
import com.iksling.blog.util.*;
import com.iksling.blog.vo.MultiFileVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

import static com.iksling.blog.constant.FlagConst.DELETED;
import static com.iksling.blog.enums.FileDirEnum.AUDIO_CHAT;
import static com.iksling.blog.util.CommonUtil.getSplitStringByIndex;

/**
 *
 */
@Service
public class ChatRecordServiceImpl extends ServiceImpl<ChatRecordMapper, ChatRecord>
    implements ChatRecordService{

    @Autowired
    private ChatRecordMapper chatRecordMapper;

    @Autowired
    private MultiFileMapper multiFileMapper;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WebSocketListener webSocketListener;

    @Resource
    private HttpServletRequest request;

    @Override
    @Transactional
    public void saveChatRecord(String chatContent) {
        Object o = JSON.parseObject(chatContent, Map.class).get("chatContent");
        if (o == null)
            throw new OperationStatusException();
        chatContent = o.toString().trim();
        if (chatContent.equals(""))
            throw new OperationStatusException();
        Integer loginUserId = UserUtil.getLoginUser().getUserId();
        String ipAddress = IpUtil.getIpAddress(request);
        String ipSource = IpUtil.getIpSource(ipAddress);
        ChatRecord chatRecord = ChatRecord.builder()
                .userId(loginUserId)
                .chatType(3)
                .chatContent(chatContent)
                .ipSource(ipSource)
                .ipAddress(ipAddress)
                .createUser(loginUserId)
                .createTime(new Date()).build();
        if (loginUserId != -1) {
            User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                    .select(User::getAvatar, User::getNickname)
                    .eq(User::getId, loginUserId));
            chatRecord.setAvatar(user.getAvatar());
            chatRecord.setNickname(user.getNickname());
        }
        chatRecordMapper.insert(chatRecord);
        try {
            webSocketListener.sendChatRecord(BeanCopyUtil.copyObject(chatRecord, ChatRecordsDTO.class));
        } catch (IOException e) {
            throw new OperationStatusException("发送失败!");
        }
    }

    @Override
    @Transactional
    public void saveChatRecordVoice(MultiFileVO multiFileVO) {
        MultipartFile file = multiFileVO.getFile();
        multiFileVO.setFile(null);
        MultiFileUtil.checkValidFile(file, AUDIO_CHAT, true);
        Integer loginUserId = UserUtil.getLoginUser().getUserId();
        MultiFile multiFile = multiFileMapper.selectOne(new LambdaQueryWrapper<MultiFile>()
                .select(MultiFile::getId, MultiFile::getFileFullPath)
                .eq(MultiFile::getUserId, loginUserId)
                .eq(MultiFile::getFileName, AUDIO_CHAT.getCurrentPath())
                .eq(MultiFile::getDeletedCount, 0));
        if (multiFile == null)
            throw new OperationStatusException();
        String[] originalFilenameArr = Objects.requireNonNull(file.getOriginalFilename()).split("\\.");
        long fileName = IdWorker.getId();
        String targetAddr = multiFile.getFileFullPath();
        String fullFileName = fileName + "." + originalFilenameArr[1];
        String url = MultiFileUtil.upload(file, targetAddr, fullFileName);
        if (url == null)
            throw new FileStatusException("文件上传失败!");
        String ipAddress = IpUtil.getIpAddress(request);
        String ipSource = IpUtil.getIpSource(ipAddress);
        Date createTime = new Date();
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .select(User::getAvatar, User::getNickname)
                .eq(User::getId, loginUserId));
        ChatRecord chatRecord = ChatRecord.builder()
                .userId(loginUserId)
                .avatar(user.getAvatar())
                .nickname(user.getNickname())
                .chatContent(url)
                .ipSource(ipSource)
                .ipAddress(ipAddress)
                .chatType(4)
                .createUser(loginUserId)
                .createTime(createTime)
                .build();
        chatRecordMapper.insert(chatRecord);
        try {
            webSocketListener.sendChatRecordVoice(BeanCopyUtil.copyObject(chatRecord, ChatRecordsDTO.class));
        } catch (IOException e) {
            throw new OperationStatusException("发送语音失败!");
        }
        multiFileMapper.insert(MultiFile.builder()
                .userId(loginUserId)
                .parentId(multiFile.getId())
                .fileDesc("{'userId':"+loginUserId+",'info':'聊天室语音消息'}")
                .fileMark(AUDIO_CHAT.getCurrentPath().intValue())
                .fileName(fileName)
                .fileSize(file.getSize())
                .fileFullPath(targetAddr + "/" + fullFileName)
                .fileExtension(originalFilenameArr[1])
                .fileNameOrigin(originalFilenameArr[0])
                .deletableFlag(false)
                .ipSource(ipSource)
                .ipAddress(ipAddress)
                .createUser(loginUserId)
                .createTime(createTime)
                .build());
    }

    @Override
    @Transactional
    public void updateChatRecord(Integer id) {
        LoginUser loginUser = UserUtil.getLoginUser();
        ChatRecord chatRecord = chatRecordMapper.selectOne(new LambdaQueryWrapper<ChatRecord>()
                .select(ChatRecord::getUserId, ChatRecord::getChatType, ChatRecord::getChatContent)
                .eq(ChatRecord::getId, id)
                .eq(ChatRecord::getRecalledFlag, false)
                .eq(loginUser.getRoleWeight() > 200, ChatRecord::getUserId, loginUser.getUserId()));
        if (chatRecord == null)
            throw new OperationStatusException();
        Date updateTime = new Date();
        chatRecordMapper.update(null, new LambdaUpdateWrapper<ChatRecord>()
                .set(ChatRecord::getRecalledFlag, true)
                .set(ChatRecord::getUpdateUser, loginUser.getUserId())
                .set(ChatRecord::getUpdateTime, updateTime)
                .eq(ChatRecord::getId, id));
        if (chatRecord.getChatType() == 4)
            updateChatRecordBy(loginUser.getUserId(), chatRecord.getChatContent().split(FtpUtil.address)[1], updateTime);
        try {
            webSocketListener.sendChatRecordBack(id, chatRecord.getChatType());
        } catch (IOException e) {
            throw new OperationStatusException("发送语音失败!");
        }
    }

    @Override
    public Dict getChatRecordsDTO() {
        String ipAddress = IpUtil.getIpAddress(request);
        String ipSource = IpUtil.getIpSource(ipAddress);
        return Dict.create()
                .set("ipAddress", ipAddress)
                .set("ipSource", ipSource)
                .set("permitFlag", UserUtil.getLoginUser().getRoleWeight() <= 200)
                .set("chatRecordsDTOList", BeanCopyUtil.copyList(chatRecordMapper.selectList(new LambdaQueryWrapper<ChatRecord>()
                    .select(ChatRecord::getId, ChatRecord::getUserId, ChatRecord::getAvatar, ChatRecord::getNickname, ChatRecord::getChatType, ChatRecord::getChatContent, ChatRecord::getIpSource, ChatRecord::getIpAddress, ChatRecord::getCreateTime)
                    .ge(ChatRecord::getCreateTime, DateUtil.getSomeDay(new Date(), -1))
                    .eq(ChatRecord::getRecalledFlag, 0)), ChatRecordsDTO.class));
    }

    private void updateChatRecordBy(Integer loginUserId, String fileFullPath, Date updateTime) {
        long fileNameNew = IdWorker.getId();
        String[] pathArr = fileFullPath.split("\\.");
        String fileFullPathOld = pathArr[0];
        String fileFullPathNew = fileFullPathOld + "_" + fileNameNew + "_" + DELETED + "." + pathArr[pathArr.length - 1];
        multiFileMapper.update(null, new LambdaUpdateWrapper<MultiFile>()
                .set(MultiFile::getFileNameNew, fileNameNew)
                .set(MultiFile::getFileFullPath, fileFullPathNew)
                .set(MultiFile::getDeletedCount, 1)
                .set(MultiFile::getUpdateUser, loginUserId)
                .set(MultiFile::getUpdateTime, updateTime)
                .eq(MultiFile::getFileName, getSplitStringByIndex(fileFullPathOld, "/", -1)));
        MultiFileUtil.rename(fileFullPath, fileFullPathNew);
    }
}




