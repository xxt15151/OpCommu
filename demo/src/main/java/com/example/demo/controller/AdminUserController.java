package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.AdminUserManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    @Autowired
    private AdminUserManageService adminUserManageService;

    @GetMapping
    public ResponseEntity<List<User>> listUsers() {
        return ResponseEntity.ok(adminUserManageService.listAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User user = adminUserManageService.getUserById(id);
        if (user == null) return ResponseEntity.notFound().build();
        user.setPassword(null);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        if (user.getUsername() == null || user.getPassword() == null || user.getEmail() == null) {
            return ResponseEntity.badRequest().body("Missing required fields");
        }
        boolean success = adminUserManageService.createUser(user);
        if (success) {
            return ResponseEntity.status(HttpStatus.CREATED).body("User created");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create user");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        String username = payload.get("username");
        String email = payload.get("email");
        if (username == null || email == null) {
            return ResponseEntity.badRequest().body("Username and email are required");
        }
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(email);
        boolean success = adminUserManageService.updateUser(user);
        if (success) {
            return ResponseEntity.ok("User updated");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update user");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        boolean success = adminUserManageService.deleteUser(id);
        if (success) {
            return ResponseEntity.ok("User deleted");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete user");
        }
    }
}