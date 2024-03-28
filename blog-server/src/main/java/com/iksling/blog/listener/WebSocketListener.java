package com.iksling.blog.listener;

import com.alibaba.fastjson.JSON;
import com.iksling.blog.dto.ChatRecordsDTO;
import com.iksling.blog.pojo.Dict;
import com.iksling.blog.vo.WebSocketMessageVO;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint(value = "/websocket")
public class WebSocketListener {
    private Session session;
    private static CopyOnWriteArraySet<WebSocketListener> webSocketSet = new CopyOnWriteArraySet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        WebSocketListener that = (WebSocketListener) o;
        return Objects.equals(session, that.session);
    }

    @Override
    public int hashCode() {
        return Objects.hash(session);
    }

    @OnOpen
    public void onOpen(Session session) throws IOException {
        this.session = session;
        webSocketSet.add(this);
        updateOnlineCount();
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        WebSocketMessageVO webSocketMessageVO = JSON.parseObject(message, WebSocketMessageVO.class);
        if (webSocketMessageVO.getType() == 9) {
            webSocketMessageVO.setData("pong");
            session.getBasicRemote().sendText(JSON.toJSONString(JSON.toJSONString(webSocketMessageVO)));
        }
    }

    @OnClose
    public void onClose() throws IOException {
        webSocketSet.remove(this);
        updateOnlineCount();
    }

    private void updateOnlineCount() throws IOException {
        WebSocketMessageVO webSocketMessageVO = new WebSocketMessageVO();
        webSocketMessageVO.setType(1);
        webSocketMessageVO.setData(webSocketSet.size());
        broadcastMessage(webSocketMessageVO);
    }

    public void sendChatRecord(ChatRecordsDTO chatRecordsDTO) throws IOException {
        WebSocketMessageVO webSocketMessageVO = new WebSocketMessageVO();
        webSocketMessageVO.setType(3);
        webSocketMessageVO.setData(chatRecordsDTO);
        broadcastMessage(webSocketMessageVO);
    }

    public void sendChatRecordVoice(ChatRecordsDTO chatRecordsDTO) throws IOException {
        WebSocketMessageVO webSocketMessageVO = new WebSocketMessageVO();
        webSocketMessageVO.setType(4);
        webSocketMessageVO.setData(chatRecordsDTO);
        broadcastMessage(webSocketMessageVO);
    }

    public void sendChatRecordBack(Integer id, Integer chatType) throws IOException {
        WebSocketMessageVO webSocketMessageVO = new WebSocketMessageVO();
        webSocketMessageVO.setType(5);
        webSocketMessageVO.setData(Dict.create().set("id", id).set("chatType", chatType));
        broadcastMessage(webSocketMessageVO);
    }

    private void broadcastMessage(Object o) throws IOException {
        for (WebSocketListener webSocketListener : webSocketSet) {
            synchronized (webSocketListener.session) {
                webSocketListener.session.getBasicRemote().sendText(JSON.toJSONString(o));
            }
        }
    }
}
