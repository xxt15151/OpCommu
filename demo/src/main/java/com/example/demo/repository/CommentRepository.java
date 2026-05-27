package com.example.demo.repository;

import com.example.demo.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CommentRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int save(Comment comment) {
        String sql = "INSERT INTO comment (content, user_id, article_id, parent_id) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql, comment.getContent(), comment.getUserId(), comment.getArticleId(), comment.getParentId());
    }

    public List<Comment> findByArticleId(Long articleId) {
        String sql = "SELECT id, content, user_id as userId, article_id as articleId, parent_id as parentId, created_at as createdAt FROM comment WHERE article_id = ? ORDER BY created_at ASC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Comment.class), articleId);
    }
}