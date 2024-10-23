package com.messranger.services;

import com.messranger.entity.Message;

import java.util.List;
import java.util.Optional;

public interface MessageService {
    Message send(String chatId, String senderId, String content);
    Message update(String id, String content, boolean isDeleted, boolean isRead);
    Optional<Message> getById(String id);
    void delete(String id);
    List<Message> getAll();
    List<Message> search(String chatId);
}
