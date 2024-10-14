package com.messranger.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Message extends  Identifier{
    private String chatId;
    private String messageText;

    private LocalDate date = LocalDate.now();
    private LocalTime time = LocalTime.now();

    private boolean isPinned;

    public Message(String chatId, String messageText, LocalDate date, LocalTime time, boolean isPinned) {
        this.chatId = chatId;
        this.messageText = messageText;
        this.date = date;
        this.time = time;
        this.isPinned = isPinned;
    }

    public String getChatId() {
        return chatId;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public boolean isPinned() {
        return isPinned;
    }

    public void setPinned(boolean pinned) {
        isPinned = pinned;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }
}
