package com.messranger.services;

import com.messranger.entity.Chat;
import com.messranger.repositories.ChatRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChatService extends BaseService<Chat>{
    private static final Logger LOGGER = LoggerFactory.getLogger(ChatService.class);

    public ChatService() {
        repository = new ChatRepository(dbConfig.getDataSource());
    }

    @Override
    public Chat save(Chat instance) {
        LOGGER.info("Saving chat with type: {}", instance.getType());
        Chat chat;
        switch (instance.getType()){
            case "p2p":
                if(repository.find(instance.getId()).isEmpty()) {
                    chat = new Chat(instance.getType(), instance.getCreatedBy(), null, null, true, LocalDateTime.now());
                    return repository.save(chat);
                }
                break;
            case "group":
                if(repository.find(instance.getId()).isEmpty()) {
                    chat = new Chat(instance.getType(), instance.getCreatedBy(), instance.getName(), instance.getDescription(), true, LocalDateTime.now());
                    return repository.save(chat);
                }
                break;
            case "channel":
                if(repository.find(instance.getId()).isEmpty()) {
                    chat = new Chat(instance.getType(), instance.getCreatedBy(), instance.getName(), instance.getDescription(), false, LocalDateTime.now());
                    return repository.save(chat);
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid chat type");
        }
        return null;
    }

    @Override
    public Chat update(Chat instance) {
        Chat chat = repository.find(instance.getId()).orElseThrow();
        chat.setId(instance.getId());
        chat.setType(instance.getType());
        chat.setCreatedBy(instance.getCreatedBy());
        chat.setName(instance.getName());
        chat.setDescription(instance.getDescription());
        chat.setPrivate(instance.isPrivate());
        return repository.update(chat);
    }

    @Override
    public void delete(String id) {
        LOGGER.info("Deleting chat with ID: {}", id);
        if(!repository.find(id).isEmpty()){
            repository.delete(id);
        }
    }
}
