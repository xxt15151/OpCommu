package com.example.demo.repository;

import com.example.demo.model.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ArticleRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int save(Article article) {
        String sql = "INSERT INTO article (title, content, user_id, group_id) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql, article.getTitle(), article.getContent(), article.getUserId(), article.getGroupId());
    }

    public Article findById(Long id) {
        String sql = "SELECT id, title, content, user_id as userId, group_id as groupId, view_count as viewCount, like_count as likeCount, created_at as createdAt, updated_at as updatedAt FROM article WHERE id = ?";
        List<Article> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Article.class), id);
        return list.isEmpty() ? null : list.get(0);
    }

    public List<Article> findAll(int offset, int limit) {
        String sql = "SELECT id, title, content, user_id as userId, group_id as groupId, view_count as viewCount, like_count as likeCount, created_at as createdAt, updated_at as updatedAt FROM article ORDER BY created_at DESC LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Article.class), limit, offset);
    }

    public List<Article> findByUserId(Long userId, int offset, int limit) {
        String sql = "SELECT id, title, content, user_id as userId, group_id as groupId, view_count as viewCount, like_count as likeCount, created_at as createdAt, updated_at as updatedAt FROM article WHERE user_id = ? ORDER BY created_at DESC LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Article.class), userId, limit, offset);
    }

    public void incrementViewCount(Long id) {
        String sql = "UPDATE article SET view_count = view_count + 1 WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void updateLikeCount(Long id, Long delta) {
        String sql = "UPDATE article SET like_count = like_count + ? WHERE id = ?";
        jdbcTemplate.update(sql, delta, id);
    }
}