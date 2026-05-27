package com.example.demo.repository;

import com.example.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AdminUserManageRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<User> findAllUsers() {
        String sql = "SELECT id, username, password, email, created_at as createdAt FROM app_user";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class));
    }

    public User findUserById(Long id) {
        String sql = "SELECT id, username, password, email, created_at as createdAt FROM app_user WHERE id = ?";
        List<User> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), id);
        return list.isEmpty() ? null : list.get(0);
    }

    public int deleteUserById(Long id) {
        String sql = "DELETE FROM app_user WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public int saveUser(User user) {
        String sql = "INSERT INTO app_user (username, password, email) VALUES (?, ?, ?)";
        return jdbcTemplate.update(sql, user.getUsername(), user.getPassword(), user.getEmail());
    }

    public int updateUser(User user) {
        String sql = "UPDATE app_user SET username = ?, email = ? WHERE id = ?";
        return jdbcTemplate.update(sql, user.getUsername(), user.getEmail(), user.getId());
    }
}