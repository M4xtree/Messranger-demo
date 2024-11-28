package com.messranger.services;

import com.messranger.entity.User;
import com.messranger.services.MessageService;
import com.messranger.services.MembersService;
import com.messranger.model.PageRequest;
import com.messranger.entity.Members;
import com.messranger.entity.Message;
import com.messranger.entity.Chat;
import com.messranger.services.ChatService;

import com.messranger.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


public class UserService extends BaseService<User>{
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private MembersService membersService;
    private MessageService messageService;
    private ChatService chatService;

    public UserService() {
        repository = new UserRepository(dbConfig.getDataSource());
        membersService = new MembersService();
        messageService = new MessageService();
        chatService = new ChatService();
    }

    @Override
    public User update(User instance) {
        LOGGER.info("Updating user with ID: {}", instance.getId());
        User repUser = repository.find(instance.getId()).orElseThrow();

        if (instance.getNickname() == null || instance.getNickname().isEmpty()) {
            throw new IllegalArgumentException("Nickname cannot be null or empty");
        }
        if (instance.getPhoneNumber() == null || instance.getPhoneNumber().isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be null or empty");
        }

        repUser.setId(instance.getId());
        repUser.setNickname(instance.getNickname());
        repUser.setPhoneNumber(instance.getPhoneNumber());
        return repository.update(repUser);
    }

    @Override
    public User save(User instance) {
        LOGGER.info("Registering new user with nickname: {}", instance.getNickname());
        if (repository.find(instance.getId()).isEmpty()) {
            if (instance.getNickname() == null || instance.getNickname().isEmpty()) {
                throw new IllegalArgumentException("Nickname cannot be null or empty");
            }
            if (instance.getPhoneNumber() == null || instance.getPhoneNumber().isEmpty()) {
                throw new IllegalArgumentException("Phone number cannot be null or empty");
            }
            return repository.save(instance);
        }
        return null;
    }

    @Override
    public void delete(String id) {
        LOGGER.info("Deleting user with ID: {}", id);
        if (!repository.find(id).isEmpty()) {
            List<Message> messages = messageService.findAll(new PageRequest(0, Long.MAX_VALUE, new ArrayList<>()), new Message(null, id, null, null, false, false, null));
            if (!messages.isEmpty()) {
                messages.forEach(message -> messageService.delete(message.getId()));
            }

            List<Members> members = membersService.findAll(new PageRequest(0, Long.MAX_VALUE, new ArrayList<>()), new Members(null, id, null, false, false, false, null, null));
            if (!members.isEmpty()) {
                members.forEach(member -> membersService.delete(member.getChatId(), member.getUserId()));
            }

            List<Chat> chats = chatService.findAll(new PageRequest(0, Long.MAX_VALUE, new ArrayList<>()), new Chat(null, id, null, null, false, null));
            if (!chats.isEmpty()) {
                chats.forEach(chat -> chatService.delete(chat.getId()));
            }

            repository.delete(id);
        }
    }
}
