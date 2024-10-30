package com.messranger.services;

import com.messranger.entity.Message;
import com.messranger.repositories.MessageRepository;

import java.time.LocalDateTime;
import java.util.Optional;


public class MessageService extends BaseService<Message> {
    public MessageService() {
        repository = new MessageRepository(dbConfig.getDataSource());
    }

    @Override
    public Message update(Message instance){
        Optional<Message> existingMessage = repository.find(instance.getId());
        if(existingMessage.isPresent()) {
            Message message = existingMessage.get();
            message.setContent(instance.getContent());
            message.setDeleted(instance.isDeleted());
            message.setRead(instance.isRead());
            message.setEditedAt(LocalDateTime.now());
            return repository.update(message);
        }
        return null;
    }
}
