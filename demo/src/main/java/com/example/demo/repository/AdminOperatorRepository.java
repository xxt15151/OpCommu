package com.example.demo.repository;

import com.example.demo.model.Operator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AdminOperatorRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Operator> findAll() {
        String sql = "SELECT id, name, attributes, created_by as createdBy, created_at as createdAt FROM operator";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Operator.class));
    }

    public Operator findById(Long id) {
        String sql = "SELECT id, name, attributes, created_by as createdBy, created_at as createdAt FROM operator WHERE id = ?";
        List<Operator> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Operator.class), id);
        return list.isEmpty() ? null : list.get(0);
    }

    public int save(Operator operator) {
        String sql = "INSERT INTO operator (name, attributes, created_by) VALUES (?, ?, ?)";
        return jdbcTemplate.update(sql, operator.getName(), operator.getAttributes(), operator.getCreatedBy());
    }

    public int update(Operator operator) {
        String sql = "UPDATE operator SET name = ?, attributes = ? WHERE id = ?";
        return jdbcTemplate.update(sql, operator.getName(), operator.getAttributes(), operator.getId());
    }

    public int deleteById(Long id) {
        String sql = "DELETE FROM operator WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}