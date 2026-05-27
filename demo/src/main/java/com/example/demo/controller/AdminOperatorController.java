package com.example.demo.controller;

import com.example.demo.model.Operator;
import com.example.demo.service.AdminOperatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/operators")
public class AdminOperatorController {

    @Autowired
    private AdminOperatorService adminOperatorService;

    // 获取所有算子
    @GetMapping
    public ResponseEntity<List<Operator>> listOperators() {
        return ResponseEntity.ok(adminOperatorService.listAllOperators());
    }

    // 获取单个算子
    @GetMapping("/{id}")
    public ResponseEntity<Operator> getOperator(@PathVariable Long id) {
        Operator op = adminOperatorService.getOperatorById(id);
        if (op == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(op);
    }

    // 创建算子
    @PostMapping
    public ResponseEntity<?> createOperator(@RequestBody Map<String, Object> payload) {
        try {
            String name = (String) payload.get("name");
            String attributes = (String) payload.get("attributes");
            Number createdBy = (Number) payload.get("createdBy");
            if (name == null || attributes == null) {
                return ResponseEntity.badRequest().body("Missing name or attributes");
            }
            Operator operator = new Operator();
            operator.setName(name);
            operator.setAttributes(attributes);
            if (createdBy != null) operator.setCreatedBy(createdBy.longValue());
            boolean success = adminOperatorService.createOperator(operator);
            if (success) {
                return ResponseEntity.status(HttpStatus.CREATED).body("Operator created");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create operator");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Invalid request");
        }
    }

    // 更新算子
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOperator(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        try {
            String name = (String) payload.get("name");
            String attributes = (String) payload.get("attributes");
            if (name == null || attributes == null) {
                return ResponseEntity.badRequest().body("Missing name or attributes");
            }
            Operator operator = new Operator();
            operator.setId(id);
            operator.setName(name);
            operator.setAttributes(attributes);
            boolean success = adminOperatorService.updateOperator(operator);
            if (success) {
                return ResponseEntity.ok("Operator updated");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update operator");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid request");
        }
    }

    // 删除算子
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOperator(@PathVariable Long id) {
        boolean success = adminOperatorService.deleteOperator(id);
        if (success) {
            return ResponseEntity.ok("Operator deleted");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete operator");
        }
    }
}