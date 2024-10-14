package com.messranger.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

public class Chat extends Identifier {
    private String type;  // 'p2p', 'group', 'channel'
    private String createdBy;
    private String name;
    private String description;
    private boolean isPrivate;
    private LocalDateTime createdAt;

    public Chat(String type, String createdBy, String name, String description, boolean isPrivate, LocalDateTime createdAt) {
        this.type = type;
        this.createdBy = createdBy;
        this.name = name;
        this.description = description;
        this.isPrivate = isPrivate;
        this.createdAt = createdAt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
