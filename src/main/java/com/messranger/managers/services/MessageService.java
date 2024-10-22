package com.messranger.managers.services;

import com.messranger.entity.Message;
import com.messranger.managers.repositories.MessageRepository;

public class MessageService extends BaseService<Message>{

    public MessageService(MessageRepository messageRepository) {
        super(messageRepository);
    }
}