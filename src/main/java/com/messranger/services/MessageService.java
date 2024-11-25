package com.messranger.services;

import com.messranger.entity.Message;
import com.messranger.model.PageRequest;
import com.messranger.repositories.MessageRepository;

import java.time.LocalDateTime;
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
        if(repository.find(instance.getId()).isEmpty()) {
            Message message = new Message(instance.getChatId(), instance.getSenderId(), instance.getContent(), LocalDateTime.now(), false, false, null);
            return repository.save(message);
        }
        return null;
    }

    @Override
    public List<Message> findAll(PageRequest pageRequest, Message filter) {
        LOGGER.info("Getting messages for chat with ID: {}", filter.getChatId());
        Message newFilter = repository.find(filter.getId()).orElseThrow();
        newFilter.setChatId(filter.getChatId());
        return repository.findAll(pageRequest, filter);
    }
}
