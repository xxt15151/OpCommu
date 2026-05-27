package com.example.demo.repository;

import com.example.demo.model.Operator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OperatorRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Operator> findAll() {
        String sql = "SELECT id, name, attributes, created_by as createdBy, created_at as createdAt FROM operator";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Operator.class));
    }
}