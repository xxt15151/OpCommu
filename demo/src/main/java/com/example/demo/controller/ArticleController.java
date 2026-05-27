package com.example.demo.controller;

import com.example.demo.model.Article;
import com.example.demo.model.Comment;
import com.example.demo.service.ArticleService;
import com.example.demo.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CommentService commentService;

    // 发布文章（需要登录，请求头 X-User-Id）
    @PostMapping
    public ResponseEntity<?> createArticle(@RequestBody Map<String, Object> payload,
                                           @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing user id");
        }
        String title = (String) payload.get("title");
        String content = (String) payload.get("content");
        Object groupIdObj = payload.get("groupId");
        Long groupId = groupIdObj != null ? ((Number) groupIdObj).longValue() : null;

        if (title == null || content == null) {
            return ResponseEntity.badRequest().body("Title and content are required");
        }

        Article article = new Article();
        article.setTitle(title);
        article.setContent(content);
        article.setUserId(userId);
        article.setGroupId(groupId);

        boolean success = articleService.createArticle(article);
        if (success) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Article created");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create article");
        }
    }

    // 文章列表（分页，公开）
    @GetMapping
    public ResponseEntity<List<Article>> listArticles(@RequestParam(defaultValue = "1") int page,
                                                      @RequestParam(defaultValue = "10") int size) {
        List<Article> articles = articleService.listArticles(page, size);
        return ResponseEntity.ok(articles);
    }

    // 查询某用户发布的文章
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Article>> listArticlesByUser(@PathVariable Long userId,
                                                            @RequestParam(defaultValue = "1") int page,
                                                            @RequestParam(defaultValue = "10") int size) {
        List<Article> articles = articleService.listArticlesByUser(userId, page, size);
        return ResponseEntity.ok(articles);
    }

    // 文章详情（会增加浏览量）
    @GetMapping("/{id}")
    public ResponseEntity<Article> getArticle(@PathVariable Long id) {
        Article article = articleService.getArticleDetail(id);
        if (article == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(article);
    }

    // 点赞/取消点赞（需要登录）
    @PostMapping("/{id}/like")
    public ResponseEntity<?> toggleLike(@PathVariable Long id,
                                        @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing user id");
        }
        boolean liked = articleService.toggleLike(userId, id);
        String message = liked ? "Liked" : "Unliked";
        return ResponseEntity.ok(Map.of("message", message));
    }

    // 查询点赞量
    @GetMapping("/{id}/likes")
    public ResponseEntity<Map<String, Long>> getLikeCount(@PathVariable Long id) {
        Long count = articleService.getLikeCount(id);
        return ResponseEntity.ok(Map.of("likeCount", count));
    }

    // 发表评论（需要登录）
    @PostMapping("/{id}/comments")
    public ResponseEntity<?> addComment(@PathVariable Long id,
                                        @RequestBody Map<String, Object> payload,
                                        @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing user id");
        }
        String content = (String) payload.get("content");
        Object parentIdObj = payload.get("parentId");
        Long parentId = parentIdObj != null ? ((Number) parentIdObj).longValue() : null;

        if (content == null || content.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Comment content cannot be empty");
        }

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setUserId(userId);
        comment.setArticleId(id);
        comment.setParentId(parentId);

        boolean success = commentService.addComment(comment);
        if (success) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Comment added");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add comment");
        }
    }

    // 获取文章的评论列表
    @GetMapping("/{id}/comments")
    public ResponseEntity<List<Comment>> getComments(@PathVariable Long id) {
        List<Comment> comments = commentService.getCommentsByArticle(id);
        return ResponseEntity.ok(comments);
    }
}