package com.messranger.services;

import com.messranger.entity.Chat;
import com.messranger.entity.Message;
import com.messranger.entity.Members;
import com.messranger.entity.User;
import com.messranger.model.PageRequest;
import com.messranger.repositories.ChatRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ChatService extends BaseService<Chat> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChatService.class);

    private final UserService userService;
    private final MembersService membersService;
    private final MessageService messageService;

    public ChatService() {
        repository = new ChatRepository(dbConfig.getDataSource());
        this.userService = new UserService();
        this.membersService = new MembersService();
        this.messageService = new MessageService();
    }

    @Override
    public Chat save(Chat instance) {
        LOGGER.info("Saving chat with type: {}", instance.getType());
        if (instance.getType() == null || instance.getType().isEmpty()) {
            throw new IllegalArgumentException("Chat type cannot be null or empty");
        }
        if (instance.getCreatedBy() == null || instance.getCreatedBy().isEmpty()) {
            throw new IllegalArgumentException("CreatedBy cannot be null or empty");
        }
        if (repository.find(instance.getId()).isEmpty()) {
            switch (instance.getType()) {
                case "p2p":
                    instance.setPrivate(true);
                    break;
                case "group":
                    instance.setPrivate(true);
                    break;
                case "channel":
                    instance.setPrivate(false);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid chat type");
            }
            instance.setCreatedAt(LocalDateTime.now());
            Chat chat = repository.save(instance);
            if (instance.getType().equals("p2p")) {
                membersService.save(new Members(chat.getId(), instance.getCreatedBy(), "member", false, false, false, null, LocalDateTime.now()));
            } else {
                membersService.save(new Members(chat.getId(), instance.getCreatedBy(), "creator", true, true, true, null, LocalDateTime.now()));
            }
            return chat;
        }
        return null;
    }

    @Override
    public Chat update(Chat instance) {
        LOGGER.info("Updating chat with ID: {}", instance.getId());
        Chat chat = repository.find(instance.getId()).orElseThrow(() -> new IllegalArgumentException("Chat not found"));

        if (instance.getType() == null || instance.getType().isEmpty()) {
            throw new IllegalArgumentException("Chat type cannot be null or empty");
        }
        if (instance.getCreatedBy() == null || instance.getCreatedBy().isEmpty()) {
            throw new IllegalArgumentException("CreatedBy cannot be null or empty");
        }

        chat.setType(instance.getType());
        chat.setCreatedBy(instance.getCreatedBy());
        chat.setName(instance.getName());
        chat.setDescription(instance.getDescription());
        chat.setPrivate(instance.isPrivate());
        chat.setCreatedAt(instance.getCreatedAt());

        return repository.update(chat);
    }

    @Override
    public void delete(String id) {
        LOGGER.info("Deleting chat with ID: {}", id);
        if (!repository.find(id).isEmpty()) {
            List<Message> messages = messageService.findAll(new PageRequest(0, Long.MAX_VALUE, new ArrayList<>()), new Message(id, null, null, null, false, false, null));
            if (!messages.isEmpty()) {
                messages.forEach(message -> messageService.delete(message.getId()));
            }

            List<Members> members = membersService.findAll(new PageRequest(0, Long.MAX_VALUE, new ArrayList<>()), new Members(id, null, null, false, false, false, null, null));
            if (!members.isEmpty()) {
                members.forEach(member -> membersService.delete(member.getChatId(), member.getUserId()));
            }

            repository.delete(id);
        }
    }

    public UserService getUserService() {
        return userService;
    }

    public MembersService getMembersService() {
        return membersService;
    }

    public MessageService getMessageService() {
        return messageService;
    }
}
