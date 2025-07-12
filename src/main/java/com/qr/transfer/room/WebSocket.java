package com.qr.transfer.room;

import com.qr.transfer.utils.DateUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import static com.qr.transfer.constant.UserType.*;
import static com.qr.transfer.room.Room.LINK_CACHE;
import static com.qr.transfer.room.Room.WEB_SOCKET_MAP;

@Component
@ServerEndpoint("/room/{qrCode}/{userType}")
public class WebSocket {

    public Session session;
    
    // 聊天室 Key
    public String qrCode;
    
    public String userType;
    
    private static final String PREFIX = "https://www.kiwa-tech.com";

    @OnOpen
    public void onOpen(Session session, @PathParam("qrCode") String qrCode, @PathParam("userType") String userType) throws IOException {
        this.session = session;
        this.qrCode = qrCode;
        this.userType = userType;

        if (StringUtils.isEmpty(qrCode)) {
            this.sendMessage("请输入正确的聊天室！" + DateUtils.now());
            return;
        }

        // 如果聊天室不存在，自动创建
        if (!WEB_SOCKET_MAP.containsKey(qrCode)) {
            Room.createRoom(qrCode);
        }

        Map<String, WebSocket> map = WEB_SOCKET_MAP.get(qrCode);
        WebSocket webSocket = map.get(userType);
        if (!Objects.isNull(webSocket)) {
            webSocket.session.close();
        }
        map.put(userType, this);
        this.sendMessage(session, userType + ":您已进入聊天室:" + qrCode + "=>" + DateUtils.now());
        session.getUserProperties().put("qrcode", qrCode);
        session.getUserProperties().put("userType", userType);
        this.openSend(qrCode, userType, map);
    }

    private void openSend(String qrCode, String userType, Map<String, WebSocket> newMap) {
        WebSocket admin = newMap.get(ADMIN.getUserType());

        if (Objects.nonNull(admin)) {
            Map<String, WebSocket> roomMap = WEB_SOCKET_MAP.get(qrCode);
            if (MapUtils.isEmpty(roomMap)) {
                return;
            }
            roomMap.forEach((k, webSocket) -> {
                Session ws = webSocket.session;
                Map<String, Object> objectMap = ws.getUserProperties();
                String type = (String) objectMap.get("userType");
                this.sendMessage(admin.session, "openSend" + type + "=" + DateUtils.now());
            });
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        this.session = session;
        if (StringUtils.isEmpty(qrCode)) {
            this.sendMessage("请选择聊天室!");
            return;
        }
        Map<String, WebSocket> socketMap = WEB_SOCKET_MAP.get(qrCode);
        if (MapUtils.isEmpty(socketMap)) {
            this.sendMessage("聊天室不存在!");
            return;
        }
        WebSocket admin = socketMap.get(ADMIN.getUserType());
        WebSocket customer = socketMap.get(CUSTOMER.getUserType());
        WebSocket proxy = socketMap.get(PROXY.getUserType());

        if (Objects.isNull(customer)) {
            if (Objects.nonNull(admin)) {
                Session adminsession = admin.session;
                this.sendMessage(adminsession, qrCode + ":" + userType + "=该房间客户不在线！");
            }
            System.out.println(qrCode + ": 该房间客户不在线！");
            return;
        }

        if (Objects.isNull(proxy)) {
            if (Objects.nonNull(admin)) {
                Session adminsession = admin.session;
                this.sendMessage(adminsession, qrCode + ":" + userType + "=该房间代理不在线！");
            }
            System.out.println(qrCode + ": 该房间代理不在线！");
            return;
        }

        if (StringUtils.equals(CUSTOMER.getUserType(), userType)) {
            if (message.startsWith(PREFIX)) {
                //客户端发送信息
                boolean contains = LINK_CACHE.contains(message);
                if (contains) {
                    System.out.println("已包含连接！");
                    return;
                }
                LINK_CACHE.add(message);
                if (Objects.nonNull(admin)) {
                    this.sendMessage(admin.session, message);
                }
                System.out.println("发送代理端！");
                this.sendMessage(proxy.session, message);
            }
        }
    }

    @OnClose
    public void onClose() throws IOException {
        if (StringUtils.isAnyEmpty(qrCode, userType)) {
            return;
        }
        Map<String, WebSocket> socketMap = WEB_SOCKET_MAP.get(qrCode);
        if (MapUtils.isEmpty(socketMap)) {
            return;
        }
        WebSocket webSocket = socketMap.get(userType);
        if (Objects.nonNull(webSocket)) {
            webSocket.session.close();
        }
        socketMap.remove(userType);
    }

    public void sendMessage(String message) {
        try {
            System.out.println(message);
            session.getBasicRemote().sendText("#" + message + '#');
        } catch (IOException e) {
            // skip
            System.out.println("sendMessage错误：" + message);
        }
    }

    public void sendMessage(Session session, String message) {
        try {
            System.out.println(message);
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            System.out.println("sendMessage错误：" + message);
        }
    }
} 