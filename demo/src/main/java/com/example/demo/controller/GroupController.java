package com.example.demo.controller;

import com.example.demo.model.Group;
import com.example.demo.service.GroupService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 创建组（需要登录）
     * 请求头：X-User-Id: {用户ID}
     * 请求体：{ "name": "组名称", "operatorIds": [1, 2, 3] }
     */
    @PostMapping
    public ResponseEntity<?> createGroup(@RequestBody Map<String, Object> payload,
                                         @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing user id");
        }
        try {
            String name = (String) payload.get("name");
            if (name == null || name.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Group name cannot be empty");
            }
            Object idsObj = payload.get("operatorIds");
            if (idsObj == null) {
                return ResponseEntity.badRequest().body("operatorIds is required");
            }
            String operatorIdsJson = objectMapper.writeValueAsString(idsObj);

            Group group = new Group();
            group.setName(name);
            group.setUserId(userId);
            group.setOperatorIds(operatorIdsJson);

            boolean success = groupService.createGroup(group);
            if (success) {
                return ResponseEntity.status(HttpStatus.CREATED).body("Group created");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create group");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Invalid request: " + e.getMessage());
        }
    }

    /**
     * 查询所有组（公开，无需登录）
     */
    @GetMapping
    public ResponseEntity<List<Group>> listGroups(@RequestParam(required = false) Long userId) {
        if (userId != null) {
            List<Group> groups = groupService.getGroupsByUserId(userId);
            return ResponseEntity.ok(groups);
        } else {
            List<Group> groups = groupService.getAllGroups();
            return ResponseEntity.ok(groups);
        }
    }
}