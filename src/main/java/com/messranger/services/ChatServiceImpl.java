package com.messranger.services;

import com.messranger.config.DataBaseConfig;
import com.messranger.entity.Chat;
import com.messranger.model.PageRequest;
import com.messranger.repositories.ChatRepository;

import java.util.List;
import java.util.Optional;

public class ChatServiceImpl implements ChatService{
    private final ChatRepository repository;
    private PageRequest pageRequest;

    private static final DataBaseConfig dbConfig = new DataBaseConfig();

    public ChatServiceImpl() {
        repository = new ChatRepository(dbConfig.getDataSource());
        pageRequest = new PageRequest(10, 0L, List.of("nickname ASC"));
    }

    @Override
    public Chat create(String type, String createdBy, String name, String description, boolean isPrivate, List<Integer> participantIds) {
        if (!type.equals("p2p") && !type.equals("group") && !type.equals("channel")) {
            throw new IllegalArgumentException("Invalid chat type");
        }

        Chat chat = new Chat(type, createdBy, null, null, isPrivate, null);

        if (type.equals("p2p")) {
            if (participantIds == null || participantIds.size() != 1) {
                throw new IllegalArgumentException("p2p chat requires exactly one participant");
            }
            chat.setName(null);
            chat.setDescription(null);
            Chat savedChat = repository.save(chat);
            return savedChat;
        }

        if (type.equals("group") || type.equals("channel")) {
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException(type + " chat requires a name");
            }
            chat.setName(name);
            chat.setDescription(description);

            Chat savedChat = repository.save(chat);



            if (participantIds != null && !participantIds.isEmpty()) {
                for (Integer participantId : participantIds) {
                }
            }
            return savedChat;
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
    public List<Chat> getAll() {
        return repository.findAll(pageRequest);
    }

    @Override
    public List<Chat> searchChats(String nameFilter) {
        Chat filter = new Chat(null, "", nameFilter, null, false, null);
        return repository.findAll(pageRequest, filter);
    }
}
