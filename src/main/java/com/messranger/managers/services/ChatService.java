package com.messranger.managers.services;

import com.messranger.entity.Chat;
import com.messranger.managers.repositories.ChatRepository;

public class ChatService extends BaseService<Chat>{

    public ChatService(ChatRepository chatRepository) {
        super(chatRepository);
    }
}
