package com.messranger.services;

import com.messranger.config.DataBaseConfig;
import com.messranger.entity.Chat;
import com.messranger.entity.User;
import com.messranger.model.PageRequest;
import com.messranger.repositories.ChatRepository;
import com.messranger.repositories.MembersRepository;
import com.messranger.repositories.MessageRepository;
import com.messranger.repositories.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class ChatServiceImpl implements ChatService{
    private final ChatRepository repository;
    private final UserRepository userRepository;

    private static final DataBaseConfig dbConfig = new DataBaseConfig();

    public ChatServiceImpl() {
        this.userRepository = new UserRepository(dbConfig.getDataSource());
        this.repository = new ChatRepository(dbConfig.getDataSource());
    }

    @Override
    public Chat create(String type, String createdBy, String name, String description, boolean isPrivate) {
        Optional<User> creator = userRepository.find(createdBy);
        switch (type){
            case "p2p":
                if(creator.isPresent()) {
                    Chat chat = new Chat(type, creator.get().getId(), null, null, isPrivate, LocalDateTime.now());
                    return repository.save(chat);
                }
                break;
            case "group":
                if(creator.isPresent()){
                    Chat chat = new Chat(type, creator.get().getId(), name, description, isPrivate, LocalDateTime.now());
                    return repository.save(chat);
                }
                break;
            case "channel":
                if(creator.isPresent()){
                    Chat chat = new Chat(type, creator.get().getId(), name, description, isPrivate, LocalDateTime.now());
                    return repository.save(chat);
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid chat type");
        }
        return null;
    }

    @Override
    public Chat update(String id, String type, String name, String description, boolean isPrivate) {
        Optional<Chat> existingChat = repository.find(id);
        if (existingChat.isPresent()) {
            Chat chat = existingChat.get();
            chat.setType(type);
            chat.setName(name);
            chat.setDescription(description);
            chat.setPrivate(isPrivate);
            return repository.update(chat);
        }
        return null;
    }

    @Override
    public Optional<Chat> getById(String id) {
        return repository.find(id);
    }

    @Override
    public void delete(String id) {
        repository.delete(id);
    }

    @Override
    public List<Chat> getAll(Integer limit, Long offset) {
        PageRequest pageRequest = new PageRequest(limit, offset, List.of("name"));
        return repository.findAll(pageRequest);
    }

    @Override
    public List<Chat> searchChats(String nameFilter, Integer limit, Long offset) {
        Chat filter = new Chat(null, "", nameFilter, null, false, null);
        PageRequest pageRequest = new PageRequest(limit, offset, List.of("name"));
        return repository.findAll(pageRequest, filter);
    }
}
