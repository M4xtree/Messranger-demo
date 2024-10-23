package com.messranger.services;

import com.messranger.config.DataBaseConfig;
import com.messranger.entity.Message;
import com.messranger.model.PageRequest;
import com.messranger.repositories.MessageRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class MessageServiceImpl implements MessageService{
    private final MessageRepository repository;
    private PageRequest pageRequest;

    private static final DataBaseConfig dbConfig = new DataBaseConfig();

    public MessageServiceImpl() {
        repository = new MessageRepository(dbConfig.getDataSource());
        pageRequest = new PageRequest(10, 0L, List.of("nickname ASC"));
    }

    @Override
    public Message send(String chatId, String senderId, String content) {
        Message message = new Message(chatId, senderId, content, LocalDate.now(), false, false, null);
        return repository.save(message);
    }

    @Override
    public Message update(String id, String content, boolean isDeleted, boolean isRead) {
        Optional<Message> existingMessage = repository.find(id);
        if (existingMessage.isPresent()) {
            Message message = existingMessage.get();
            message.setContent(content);
            message.setDeleted(isDeleted);
            message.setRead(isRead);
            return repository.update(message);
        }
        return null;
    }

    @Override
    public Optional<Message> getById(String id) {
        return repository.find(id);
    }

    @Override
    public void delete(String id) {
        repository.delete(id);
    }

    @Override
    public List<Message> getAll() {
        return repository.findAll(pageRequest);
    }

    @Override
    public List<Message> search(String chatId) {
        Message filter = new Message(chatId, null, null, LocalDate.now(), false, false, null);
        return repository.findAll(pageRequest, filter);
    }


    public PageRequest getPageRequest() {
        return pageRequest;
    }

    public void setPageRequest(PageRequest pageRequest) {
        this.pageRequest = pageRequest;
    }
}
