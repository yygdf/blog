package com.iksling.blog.listener;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.iksling.blog.dto.ChatRecordDTO;
import com.iksling.blog.entity.ChatRecord;
import com.iksling.blog.mapper.ChatRecordMapper;
import com.iksling.blog.mapper.MultiFileMapper;
import com.iksling.blog.pojo.Result;
import com.iksling.blog.util.DateUtil;
import com.iksling.blog.util.IpUtil;
import com.iksling.blog.util.RegexUtil;
import com.iksling.blog.vo.WebSocketMessageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.websocket.*;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.server.ServerEndpointConfig;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/websocket", configurator = WebSocketListener.ChatConfigurator.class)
@Service
public class WebSocketListener {
    private Session session;
    private static CopyOnWriteArraySet<WebSocketListener> webSocketSet = new CopyOnWriteArraySet<>();

    private static ChatRecordMapper chatRecordMapper;
    private static MultiFileMapper multiFileMapper;

    @Resource
    private HttpServletRequest request;

    @Autowired
    public void setChatRecordMapper(ChatRecordMapper chatRecordMapper) {
        WebSocketListener.chatRecordMapper = chatRecordMapper;
    }

    @Autowired
    public void setMultiFileMapper(MultiFileMapper multiFileMapper) {
        WebSocketListener.multiFileMapper = multiFileMapper;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WebSocketListener that = (WebSocketListener) o;
        return Objects.equals(session, that.session);
    }

    @Override
    public int hashCode() {
        return Objects.hash(session);
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig endpointConfig) throws IOException {
        this.session = session;
        webSocketSet.add(this);
        updateOnlineCount();
        ChatRecordDTO chatRecordDTO = getChatRecordDTO(endpointConfig);
        synchronized (session) {
            session.getBasicRemote().sendText(JSON.toJSONString(Result.success().code(2).data(chatRecordDTO)));
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        WebSocketMessageVO webSocketMessageVO = JSON.parseObject(message, WebSocketMessageVO.class);
        switch (webSocketMessageVO.getType()) {
            case 3:
                ChatRecord chatRecord = JSON.parseObject(JSON.toJSONString(webSocketMessageVO.getData()), ChatRecord.class);
                chatRecord.setChatContent(RegexUtil.deleteHTMLTag(chatRecord.getChatContent()));
                chatRecord.setCreateUser(chatRecord.getUserId());
                chatRecord.setCreateTime(new Date());
                chatRecordMapper.insert(chatRecord);
                webSocketMessageVO.setData(chatRecord);
                broadcastMessage(webSocketMessageVO);
                break;
            case 4:
                Map map = JSON.parseObject(JSON.toJSONString(webSocketMessageVO.getData()), Map.class);
                chatRecordMapper.update(null, new LambdaUpdateWrapper<ChatRecord>()
                        .set(ChatRecord::getRecalledFlag, true)
                        .eq(ChatRecord::getId, map.get("id")));
                if (map.get("chatType").equals(5)) {
                    //TODO: 删除语音文件
                }
                broadcastMessage(webSocketMessageVO);
                break;
            case 6:
                webSocketMessageVO.setData("pong");
                session.getBasicRemote().sendText(JSON.toJSONString(JSON.toJSONString(webSocketMessageVO)));
            default:
                break;
        }

    }

    private void updateOnlineCount() throws IOException {
        broadcastMessage(Result.success().code(1).data(webSocketSet.size()));
    }

    private ChatRecordDTO getChatRecordDTO(EndpointConfig endpointConfig) {
        List<ChatRecord> chatRecordList = chatRecordMapper.selectList(new LambdaQueryWrapper<ChatRecord>()
                .select(ChatRecord::getId, ChatRecord::getUserId, ChatRecord::getAvatar, ChatRecord::getNickname, ChatRecord::getChatType, ChatRecord::getChatContent, ChatRecord::getIpSource, ChatRecord::getIpAddress, ChatRecord::getCreateTime)
                .ge(ChatRecord::getCreateTime, DateUtil.getSomeDay(new Date(), -1))
                .eq(ChatRecord::getRecalledFlag, 0));
        String ipAddress = endpointConfig.getUserProperties().get(ChatConfigurator.CLIENT_IP).toString();
        return ChatRecordDTO.builder()
                .chatRecordList(chatRecordList)
                .ipSource(IpUtil.getIpSource(ipAddress))
                .ipAddress(ipAddress)
                .build();
    }

    private void broadcastMessage(Object o) throws IOException {
        for (WebSocketListener webSocketListener : webSocketSet) {
            synchronized (webSocketListener.session) {
                webSocketListener.session.getBasicRemote().sendText(JSON.toJSONString(o));
            }
        }
    }

    public static class ChatConfigurator extends ServerEndpointConfig.Configurator {
        public static final String CLIENT_IP = "CLIENT_IP";

        @Override
        public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
            Map<String, List<String>> headers = request.getHeaders();
            List<String> list;
            String client_ip = "";
            if ((list = headers.get("x-real-ip")) != null && list.size() != 0)
                client_ip = list.get(0);
            if (client_ip.length() == 0 && (list = headers.get("x-forwarded-for")) != null && list.size() != 0)
                client_ip = list.get(0);
            if (client_ip.length() == 0 && (list = headers.get("forwarded")) != null && list.size() != 0)
                client_ip = list.get(0);
            sec.getUserProperties().put(CLIENT_IP, client_ip);
        }
    }
}
