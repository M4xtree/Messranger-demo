package com.messranger.services;

import com.messranger.entity.Chat;
import com.messranger.repositories.ChatRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Supplier;

public class ChatService extends BaseService<Chat>{
    public ChatService() {
        repository = new ChatRepository(dbConfig.getDataSource());
    }

    @Override
    public Chat save(Chat instance) {
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
        chat.setType(instance.getType());
        chat.setCreatedBy(instance.getCreatedBy());
        chat.setName(instance.getName());
        chat.setDescription(instance.getDescription());
        chat.setPrivate(instance.isPrivate());
        return repository.update(chat);
    }
}
