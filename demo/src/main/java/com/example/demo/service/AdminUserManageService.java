package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.AdminUserManageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminUserManageService {

    @Autowired
    private AdminUserManageRepository adminUserManageRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public List<User> listAllUsers() {
        return adminUserManageRepository.findAllUsers();
    }

    public User getUserById(Long id) {
        return adminUserManageRepository.findUserById(id);
    }

    public boolean createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        int rows = adminUserManageRepository.saveUser(user);
        return rows > 0;
    }

    public boolean updateUser(User user) {
        // 只允许修改用户名和邮箱
        int rows = adminUserManageRepository.updateUser(user);
        return rows > 0;
    }

    public boolean deleteUser(Long id) {
        int rows = adminUserManageRepository.deleteUserById(id);
        return rows > 0;
    }
}