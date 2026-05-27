package com.example.demo.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class LikeRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean existsByUserAndArticle(Long userId, Long articleId) {
        String sql = "SELECT COUNT(*) FROM article_like WHERE user_id = ? AND article_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId, articleId);
        return count != null && count > 0;
    }

    public void insert(Long userId, Long articleId) {
        String sql = "INSERT INTO article_like (user_id, article_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, userId, articleId);
    }

    public void delete(Long userId, Long articleId) {
        String sql = "DELETE FROM article_like WHERE user_id = ? AND article_id = ?";
        jdbcTemplate.update(sql, userId, articleId);
    }
}