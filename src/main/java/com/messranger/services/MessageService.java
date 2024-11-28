package com.messranger.services;

import com.messranger.entity.Message;
import com.messranger.model.PageRequest;
import com.messranger.repositories.MessageRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MessageService extends BaseService<Message> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageService.class);

    public MessageService() {
        repository = new MessageRepository(dbConfig.getDataSource());
    }

    @Override
    public Message update(Message instance){
        Message message = repository.find(instance.getId()).orElseThrow();

        if (instance.getChatId() == null || instance.getChatId().isEmpty()) {
            throw new IllegalArgumentException("Chat ID cannot be null or empty");
        }
        if (instance.getSenderId() == null || instance.getSenderId().isEmpty()) {
            throw new IllegalArgumentException("Sender ID cannot be null or empty");
        }
        if (instance.getContent() == null || instance.getContent().isEmpty()) {
            throw new IllegalArgumentException("Content cannot be null or empty");
        }
        if (instance.getCreatedAt() == null) {
            instance.setCreatedAt(LocalDateTime.now());
        }

        message.setId(instance.getId());
        message.setContent(instance.getContent());
        message.setDeleted(instance.isDeleted());
        message.setRead(instance.isRead());
        message.setEditedAt(LocalDateTime.now());
        return repository.update(message);
    }

    @Override
    public Message save(Message instance) {
        LOGGER.info("Sending message to chat with ID: {}", instance.getChatId());
        if (repository.find(instance.getId()).isEmpty()) {
            if (instance.getChatId() == null || instance.getChatId().isEmpty()) {
                throw new IllegalArgumentException("Chat ID cannot be null or empty");
            }
            if (instance.getSenderId() == null || instance.getSenderId().isEmpty()) {
                throw new IllegalArgumentException("Sender ID cannot be null or empty");
            }
            if (instance.getContent() == null || instance.getContent().isEmpty()) {
                throw new IllegalArgumentException("Content cannot be null or empty");
            }
            if (instance.getCreatedAt() == null) {
                instance.setCreatedAt(LocalDateTime.now());
            }
            return repository.save(instance);
        }
        return null;
    }

    @Override
    public List<Message> findAll(PageRequest pageRequest, Message filter) {
        LOGGER.info("Getting messages for chat with ID: {}", filter.getChatId());
        List<Message> messages = repository.findAll(pageRequest, filter);
        if (messages.isEmpty()) {
            LOGGER.warn("No messages found for the given criteria.");
            return Collections.emptyList();
        }
        return messages;
    }
}
