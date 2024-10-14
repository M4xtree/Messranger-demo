package com.messranger.entity;

import java.util.ArrayList;
import java.util.UUID;

public class Chat extends Identifier{

    private String creatorId;
    private String parentId;
    private String pinId;
    private String chat;
    private String name;
    private String chatType;

    public Chat(String creatorId, String parentId, String pinId, String chat, String name, String chatType) {
        this.creatorId = creatorId;
        this.parentId = parentId;
        this.pinId = pinId;
        this.chat = chat;
        this.name = name;
        this.chatType = chatType;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getPinId() {
        return pinId;
    }

    public void setPinId(String pinId) {
        this.pinId = pinId;
    }

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChatType() {
        return chatType;
    }

    public void setChatType(String chatType) {
        this.chatType = chatType;
    }
}
