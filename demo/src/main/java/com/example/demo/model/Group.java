package com.example.demo.model;

import java.time.LocalDateTime;

public class Group {
    private Long id;
    private String name;
    private Long userId;
    private String operatorIds;   // JSON array of Long, e.g. "[1,2,3]"
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getOperatorIds() { return operatorIds; }
    public void setOperatorIds(String operatorIds) { this.operatorIds = operatorIds; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}