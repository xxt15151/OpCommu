package com.example.demo.service;

import com.example.demo.model.AdminUser;
import com.example.demo.repository.AdminUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminAuthService {

    @Autowired
    private AdminUserRepository adminUserRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AdminUser login(String username, String rawPassword) {
        AdminUser admin = adminUserRepository.findByUsername(username);
        if (admin != null && passwordEncoder.matches(rawPassword, admin.getPassword())) {
            admin.setPassword(null);
            return admin;
        }
        return null;
    }
}