package com.qr.transfer.web;

import com.qr.transfer.room.Room;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/chatroom")
public class ChatRoomController {

    /**
     * 创建聊天室
     */
    @GetMapping("/creat")
    public String createRoom(@RequestParam("code") String code) {
        try {
            Room.createRoom(code);
            return "创建聊天室成功：" + code;
        } catch (Exception e) {
            return "创建聊天室失败：" + e.getMessage();
        }
    }

    /**
     * 解散聊天室
     */
    @GetMapping("/breakup")
    public String breakupRoom(@RequestParam("code") String code) {
        try {
            Room.removeRoom(code);
            return "解散聊天室成功：" + code;
        } catch (Exception e) {
            return "解散聊天室失败：" + e.getMessage();
        }
    }

    /**
     * 解散所有聊天室
     */
    @GetMapping("/remove")
    public String removeAllRooms() {
        try {
            Room.removeAllRooms();
            return "解散所有聊天室成功";
        } catch (Exception e) {
            return "解散所有聊天室失败：" + e.getMessage();
        }
    }

    /**
     * 查询聊天室列表
     */
    @GetMapping("/query")
    public Map<String, Object> queryRooms() {
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> roomDetails = Room.getRoomDetails();
            result.put("success", true);
            result.put("data", roomDetails);
            result.put("count", roomDetails.size());
            result.put("debug", "返回详细聊天室信息，包含用户连接状态");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * 检查聊天室是否存在
     */
    @GetMapping("/exists")
    public Map<String, Object> checkRoomExists(@RequestParam("code") String code) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean exists = Room.roomExists(code);
            result.put("success", true);
            result.put("exists", exists);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
} 