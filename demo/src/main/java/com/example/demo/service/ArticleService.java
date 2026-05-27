package com.example.demo.service;

import com.example.demo.model.Article;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private LikeRepository likeRepository;

    public boolean createArticle(Article article) {
        int rows = articleRepository.save(article);
        return rows > 0;
    }

    public Article getArticleDetail(Long id) {
        Article article = articleRepository.findById(id);
        if (article != null) {
            articleRepository.incrementViewCount(id);
        }
        return article;
    }

    public List<Article> listArticles(int page, int size) {
        int offset = (page - 1) * size;
        return articleRepository.findAll(offset, size);
    }

    public List<Article> listArticlesByUser(Long userId, int page, int size) {
        int offset = (page - 1) * size;
        return articleRepository.findByUserId(userId, offset, size);
    }

    @Transactional
    public boolean toggleLike(Long userId, Long articleId) {
        if (likeRepository.existsByUserAndArticle(userId, articleId)) {
            // 取消点赞
            likeRepository.delete(userId, articleId);
            articleRepository.updateLikeCount(articleId, -1L);
            return false; // 返回 false 表示取消点赞
        } else {
            // 点赞
            likeRepository.insert(userId, articleId);
            articleRepository.updateLikeCount(articleId, 1L);
            return true; // 返回 true 表示已点赞
        }
    }

    public Long getLikeCount(Long articleId) {
        Article article = articleRepository.findById(articleId);
        return article != null ? article.getLikeCount() : 0L;
    }
}