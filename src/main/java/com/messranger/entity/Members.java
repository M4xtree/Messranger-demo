package com.messranger.entity;

import java.time.LocalDate;

public class Members {
    private String chatId;
    private String userId;
    private String role;  //('creator', 'admin', 'moderator', 'member')
    private boolean canDeleteMessages;
    private boolean canAddParticipants;
    private boolean canEditMessages;
    private String caret;
    private LocalDate joinedAt;

    public Members(String chatId, String userId, String role, boolean canDeleteMessages,
                   boolean canAddParticipants, boolean canEditMessages, String caret, LocalDate joinedAt) {
        this.chatId = chatId;
        this.userId = userId;
        this.role = role;
        this.canDeleteMessages = canDeleteMessages;
        this.canAddParticipants = canAddParticipants;
        this.canEditMessages = canEditMessages;
        this.caret = caret;
        this.joinedAt = joinedAt;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isCanDeleteMessages() {
        return canDeleteMessages;
    }

    public void setCanDeleteMessages(boolean canDeleteMessages) {
        this.canDeleteMessages = canDeleteMessages;
    }

    public boolean isCanAddParticipants() {
        return canAddParticipants;
    }

    public void setCanAddParticipants(boolean canAddParticipants) {
        this.canAddParticipants = canAddParticipants;
    }

    public boolean isCanEditMessages() {
        return canEditMessages;
    }

    public void setCanEditMessages(boolean canEditMessages) {
        this.canEditMessages = canEditMessages;
    }

    public String getCaret() {
        return caret;
    }

    public void setCaret(String caret) {
        this.caret = caret;
    }

    public LocalDate getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(LocalDate joinedAt) {
        this.joinedAt = joinedAt;
    }
}
