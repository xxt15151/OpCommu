package com.example.demo.repository;

import com.example.demo.model.AdminUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AdminUserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public AdminUser findByUsername(String username) {
        String sql = "SELECT id, username, password, email, role, created_at as createdAt FROM admin_user WHERE username = ?";
        List<AdminUser> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(AdminUser.class), username);
        return list.isEmpty() ? null : list.get(0);
    }

    public AdminUser findById(Long id) {
        String sql = "SELECT id, username, password, email, role, created_at as createdAt FROM admin_user WHERE id = ?";
        List<AdminUser> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(AdminUser.class), id);
        return list.isEmpty() ? null : list.get(0);
    }
}