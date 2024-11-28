package com.messranger.services;

import com.messranger.entity.Chat;
import com.messranger.repositories.ChatRepository;
import com.messranger.services.MembersService;
import com.messranger.services.MessageService;
import com.messranger.model.PageRequest;
import com.messranger.entity.Members;
import com.messranger.entity.Message;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChatService extends BaseService<Chat>{
    private static final Logger LOGGER = LoggerFactory.getLogger(ChatService.class);

    private MembersService membersService;
    private MessageService messageService;

    public ChatService() {
        repository = new ChatRepository(dbConfig.getDataSource());
        membersService = new MembersService();
        messageService = new MessageService();
    }

    @Override
    public Chat save(Chat instance) {
        LOGGER.info("Saving chat with type: {}", instance.getType());
        if (repository.find(instance.getId()).isEmpty()) {
            if (instance.getType() == null || instance.getType().isEmpty()) {
                throw new IllegalArgumentException("Chat type cannot be null or empty");
            }
            if (instance.getCreatedBy() == null || instance.getCreatedBy().isEmpty()) {
                throw new IllegalArgumentException("CreatedBy cannot be null or empty");
            }
            if (instance.getCreatedAt() == null) {
                instance.setCreatedAt(LocalDateTime.now());
            }
            return repository.save(instance);
        }
        return null;
    }

    @Override
    public Chat update(Chat instance) {
        Chat chat = repository.find(instance.getId()).orElseThrow();

        if (instance.getType() == null || instance.getType().isEmpty()) {
            throw new IllegalArgumentException("Chat type cannot be null or empty");
        }
        if (instance.getCreatedBy() == null || instance.getCreatedBy().isEmpty()) {
            throw new IllegalArgumentException("CreatedBy cannot be null or empty");
        }
        if (instance.getCreatedAt() == null) {
            instance.setCreatedAt(LocalDateTime.now());
        }

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
        messageService.findAll(new PageRequest(0, Long.MAX_VALUE, new ArrayList<>()), new Message(id, null, null, null, false, false, null))
                .forEach(message -> messageService.delete(message.getId()));

        membersService.findAll(new PageRequest(0, Long.MAX_VALUE, new ArrayList<>()), new Members(id, null, null, false, false, false, null, null))
                .forEach(member -> membersService.delete(member.getChatId(), member.getUserId()));

        repository.delete(id);
    }
}
