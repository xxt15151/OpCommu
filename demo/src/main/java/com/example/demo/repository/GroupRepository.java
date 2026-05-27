package com.example.demo.repository;

import com.example.demo.model.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GroupRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int save(Group group) {
        String sql = "INSERT INTO assembly_group (name, user_id, operator_ids) VALUES (?, ?, ?)";
        return jdbcTemplate.update(sql, group.getName(), group.getUserId(), group.getOperatorIds());
    }

    public List<Group> findAll() {
        String sql = "SELECT id, name, user_id as userId, operator_ids as operatorIds, created_at as createdAt, updated_at as updatedAt FROM assembly_group ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Group.class));
    }

    public List<Group> findByUserId(Long userId) {
        String sql = "SELECT id, name, user_id as userId, operator_ids as operatorIds, created_at as createdAt, updated_at as updatedAt FROM assembly_group WHERE user_id = ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Group.class), userId);
    }
}