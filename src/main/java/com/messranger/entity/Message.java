package com.messranger.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Message extends Identifier {
    private String chatId;
    private String senderId;
    private String content;
    private LocalDate createdAt;
    private boolean isDeleted;
    private boolean isRead;
    private LocalDate editedAt;


    public Message(String chatId, String senderId, String content, LocalDate createdAt, boolean isDeleted, boolean isRead, LocalDate editedAt) {
        this.chatId = chatId;
        this.senderId = senderId;
        this.content = content;
        this.createdAt = createdAt;
        this.isDeleted = isDeleted;
        this.isRead = isRead;
        this.editedAt = editedAt;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }

    public LocalDate getEditedAt() {
        return editedAt;
    }

    public void setEditedAt(LocalDate editedAt) {
        this.editedAt = editedAt;
    }
}
