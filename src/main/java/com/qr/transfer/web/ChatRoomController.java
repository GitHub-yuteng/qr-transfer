package com.qr.transfer.web;

import com.qr.transfer.service.ChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/chatroom")
public class ChatRoomController {

    @Autowired
    private ChatRoomService chatRoomService;

    @Value("${app.base-url}")
    private String baseUrl;

    /**
     * 获取服务器配置
     */
    @GetMapping("/config")
    public ResponseEntity<Map<String, String>> getConfig() {
        Map<String, String> config = new HashMap<>();
        config.put("baseUrl", baseUrl);
        return ResponseEntity.ok(config);
    }

    /**
     * 创建聊天室
     */
    @GetMapping("/create")
    public ResponseEntity<Map<String, Object>> createRoom(@RequestParam("code") String code) {
        Map<String, Object> response = new HashMap<>();
        try {
            chatRoomService.createRoom(code);
            response.put("success", true);
            response.put("message", "创建聊天室成功：" + code);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "创建聊天室失败：" + e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    /**
     * 解散聊天室
     */
    @GetMapping("/breakup")
    public ResponseEntity<Map<String, Object>> breakupRoom(@RequestParam("code") String code) {
        Map<String, Object> response = new HashMap<>();
        try {
            chatRoomService.removeRoom(code);
            response.put("success", true);
            response.put("message", "解散聊天室成功：" + code);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "解散聊天室失败：" + e.getMessage());
        }
        return ResponseEntity.ok(response);
    }


    /**
     * 解散聊天室
     */
    @GetMapping("/breakAll")
    public ResponseEntity<String> breakAll() {
        chatRoomService.breakAll();
        return ResponseEntity.ok("所有聊天室已解散");
    }

    /**
     * 查询聊天室列表
     */
    @GetMapping("/query")
    public ResponseEntity<Map<String, String>> queryRooms() {
        Map<String, String> room = chatRoomService.getAllRoom();
        return ResponseEntity.ok(room);
    }

    /**
     * 检查聊天室是否存在
     */
    @GetMapping("/exists")
    public ResponseEntity<Map<String, Object>> checkRoomExists(@RequestParam("code") String code) {
        Map<String, Object> response = new HashMap<>();
        response.put("exists", chatRoomService.roomExists(code));
        return ResponseEntity.ok(response);
    }

    /**
     * 更新聊天室数据（代理端调用）
     */
    @PostMapping("/data")
    public ResponseEntity<Map<String, Object>> updateRoomData(
            @RequestParam("code") String code,
            @RequestParam("data") String data) {
        Map<String, Object> response = new HashMap<>();
        try {
            chatRoomService.updateRoomData(code, data);
            response.put("success", true);
            response.put("message", "数据更新成功");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "数据更新失败：" + e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    /**
     * 获取聊天室数据（客户端调用）
     */
    @GetMapping("/data")
    public ResponseEntity<String> getRoomData(@RequestParam("code") String code) {
        Map<String, String> response = new HashMap<>();
        String data = chatRoomService.getRoomData(code);
        return ResponseEntity.ok(data);
    }
} 