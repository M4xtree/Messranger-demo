package com.messranger.entity;

public class Members {
    private String userId;
    private String chatId;
    private String role;
    private String caret;

    public Members(String userId, String chatId, String role, String caret) {
        this.userId = userId;
        this.chatId = chatId;
        this.role = role;
        this.caret = caret;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCaret() {
        return caret;
    }

    public void setCaret(String caret) {
        this.caret = caret;
    }
}
