package com.messranger.managers.controllers;

import com.messranger.entity.Message;
import com.messranger.managers.model.PageRequest;
import com.messranger.managers.services.MessageService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    public Message sendMessage(String chatId, String senderId, String content) {
        Message message = new Message(chatId, senderId, content, LocalDate.now(), false, false, null);
        return messageService.save(message);
    }

    public Message updateMessage(String id, String content, boolean isDeleted, boolean isRead) {
        Optional<Message> existingMessage = messageService.find(id);
        if (existingMessage.isPresent()) {
            Message message = existingMessage.get();
            message.setContent(content);
            message.setDeleted(isDeleted);
            message.setRead(isRead);
            return messageService.update(message);
        }
        return null;
    }

    public Optional<Message> getMessageById(String id) {
        return messageService.find(id);
    }

    public void deleteMessage(String id) {
        messageService.delete(id);
    }

    public List<Message> getAllMessages(int limit, long offset) {
        PageRequest pageRequest = new PageRequest(limit, offset, List.of("created_at DESC"));
        return messageService.findAll(pageRequest);
    }

    public List<Message> searchMessages(String chatId, int limit, long offset) {
        Message filter = new Message(chatId, null, null, LocalDate.now(), false, false, null);
        PageRequest pageRequest = new PageRequest(limit, offset, List.of("created_at ASC"));
        return messageService.findAll(pageRequest, filter);
    }
}