package com.messranger.services;

import com.messranger.entity.Message;
import com.messranger.repositories.MessageRepository;

import java.time.LocalDateTime;


public class MessageService extends BaseService<Message> {
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
}
