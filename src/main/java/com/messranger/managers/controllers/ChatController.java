package com.messranger.managers.controllers;

import com.messranger.entity.Chat;
import com.messranger.managers.model.PageRequest;
import com.messranger.managers.services.ChatService;

import java.util.List;
import java.util.Optional;

public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    public Chat createChat(String type, String createdBy, String name, String description, boolean isPrivate) {
        Chat chat = new Chat(type, createdBy, name, description, isPrivate, null);
        return chatService.save(chat);
    }

    public Chat updateChat(String id, String type, String name, String description, boolean isPrivate) {
        Optional<Chat> existingChat = chatService.find(id);
        if (existingChat.isPresent()) {
            Chat chat = existingChat.get();
            chat.setType(type);
            chat.setName(name);
            chat.setDescription(description);
            chat.setPrivate(isPrivate);
            return chatService.update(chat);
        }
        return null;
    }

    public Optional<Chat> getChatById(String id) {
        return chatService.find(id);
    }

    public void deleteChat(String id) {
        chatService.delete(id);
    }

    public List<Chat> getAllChats(int limit, long offset) {
        PageRequest pageRequest = new PageRequest(limit, offset, List.of("created_at DESC"));
        return chatService.findAll(pageRequest);
    }

    public List<Chat> searchChats(String nameFilter, int limit, long offset) {
        Chat filter = new Chat(null, "", nameFilter, null, false, null);
        PageRequest pageRequest = new PageRequest(limit, offset, List.of("name ASC"));
        return chatService.findAll(pageRequest, filter);
    }
}
