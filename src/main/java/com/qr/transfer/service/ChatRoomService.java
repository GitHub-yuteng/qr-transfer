package com.qr.transfer.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChatRoomService {
    // 存储聊天室数据，key是聊天室ID，value是代理扫描的数据
    private final Map<String, String> roomDataMap = new ConcurrentHashMap<>();

    /**
     * 创建聊天室
     */
    public void createRoom(String roomId) {
        roomDataMap.put(roomId, "");
    }

    /**
     * 删除聊天室
     */
    public void removeRoom(String roomId) {
        roomDataMap.remove(roomId);
    }

    /**
     * 更新聊天室数据
     */
    public void updateRoomData(String roomId, String data) {
        roomDataMap.put(roomId, data);
    }

    /**
     * 获取聊天室数据
     */
    public String getRoomData(String roomId) {
        return roomDataMap.getOrDefault(roomId, "");
    }

    /**
     * 检查聊天室是否存在
     */
    public boolean roomExists(String roomId) {
        return roomDataMap.containsKey(roomId);
    }

    /**
     * 获取所有聊天室状态
     */
    public Map<String, String> getAllRoom() {
        return roomDataMap;
    }
} 