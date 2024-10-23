package com.messranger.services;

import com.messranger.entity.Chat;

import java.util.List;
import java.util.Optional;

public interface ChatService {
    Chat create(String type, String createdBy, String name, String description, boolean isPrivate, List<Integer> participantIds);
    Chat update(String id, String type, String name, String description, boolean isPrivate);
    Optional<Chat> getById(String id);
    void delete(String id);
    List<Chat> getAll();
    List<Chat> searchChats(String nameFilter);
}
