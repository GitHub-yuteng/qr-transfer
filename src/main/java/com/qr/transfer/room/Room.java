package com.qr.transfer.room;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class Room {

    // 存储所有聊天室的WebSocket连接
    public static final Map<String, Map<String, WebSocket>> WEB_SOCKET_MAP = new ConcurrentHashMap<>();

    // 存储已发送的链接，避免重复发送
    public static final java.util.Set<String> LINK_CACHE = ConcurrentHashMap.newKeySet();

    /**
     * 创建聊天室
     */
    public static void createRoom(String qrCode) {
        if (!WEB_SOCKET_MAP.containsKey(qrCode)) {
            WEB_SOCKET_MAP.put(qrCode, new ConcurrentHashMap<>());
        }
    }

    /**
     * 删除聊天室
     */
    public static void removeRoom(String qrCode) {
        Map<String, WebSocket> roomMap = WEB_SOCKET_MAP.get(qrCode);
        if (roomMap != null) {
            roomMap.values().forEach(webSocket -> {
                try {
                    if (webSocket.session != null && webSocket.session.isOpen()) {
                        webSocket.session.close();
                    }
                } catch (Exception e) {
                    // 忽略关闭异常
                }
            });
            WEB_SOCKET_MAP.remove(qrCode);
        }
    }

    /**
     * 删除所有聊天室
     */
    public static void removeAllRooms() {
        WEB_SOCKET_MAP.keySet().forEach(Room::removeRoom);
    }

    /**
     * 获取聊天室列表
     */
    public static java.util.Set<String> getRoomList() {
        return WEB_SOCKET_MAP.keySet();
    }

    /**
     * 检查聊天室是否存在
     */
    public static boolean roomExists(String qrCode) {
        return WEB_SOCKET_MAP.containsKey(qrCode);
    }

    /**
     * 获取聊天室详细信息，包括已连接的用户
     */
    public static Map<String, Object> getRoomDetails() {
        Map<String, Object> roomDetails = new HashMap<>();

        WEB_SOCKET_MAP.forEach((roomCode, userMap) -> {
            Map<String, Object> roomInfo = new HashMap<>();
            roomInfo.put("code", roomCode);
            roomInfo.put("userCount", userMap.size());

            // 获取已连接的用户类型
            Set<String> connectedUsers = new HashSet<>();
            userMap.forEach((userType, webSocket) -> {
                if (webSocket != null && webSocket.session != null && webSocket.session.isOpen()) {
                    connectedUsers.add(userType);
                }
            });
            roomInfo.put("connectedUsers", connectedUsers);
            roomInfo.put("userList", new ArrayList<>(connectedUsers));

            roomDetails.put(roomCode, roomInfo);
        });

        return roomDetails;
    }
} 