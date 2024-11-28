package com.messranger.services;

import com.messranger.entity.Members;
import com.messranger.repositories.MembersRepository;
import com.messranger.services.ChatService;
import com.messranger.entity.Chat;
import com.messranger.model.PageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MembersService extends BaseService<Members>{
    private static final Logger LOGGER = LoggerFactory.getLogger(MembersService.class);

    private ChatService chatService;

    public MembersService() {
        repository = new MembersRepository(dbConfig.getDataSource());
        chatService = new ChatService();
    }

    @Override
    public Members update(Members instance) {
        LOGGER.info("Updating member role in chat with ID: {}", instance.getChatId());
        Members member = repository.find(instance.getChatId(),instance.getUserId()).orElseThrow();

        if (instance.getRole() == null || instance.getRole().isEmpty()) {
            throw new IllegalArgumentException("Role cannot be null or empty");
        }
        if (instance.getJoinedAt() == null) {
            instance.setJoinedAt(LocalDateTime.now());
        }

        member.setChatId(instance.getChatId());
        member.setUserId(instance.getUserId());
        member.setRole(instance.getRole());
        member.setCanDeleteMessages(instance.isCanDeleteMessages());
        member.setCanAddParticipants(instance.isCanAddParticipants());
        member.setCanEditMessages(instance.isCanEditMessages());
        member.setCaret(instance.getCaret());
        member.setJoinedAt(LocalDateTime.now());
        return ((MembersRepository) repository).updateMembers(member);
    }

    @Override
    public Members save(Members instance) {
        LOGGER.info("Adding or updating member in chat with ID: {}", instance.getChatId());
        if (instance.getChatId() == null || instance.getChatId().isEmpty()) {
            throw new IllegalArgumentException("Chat ID cannot be null or empty");
        }
        if (instance.getUserId() == null || instance.getUserId().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }

        Optional<Members> existingMember = repository.find(instance.getChatId(), instance.getUserId());
        if (existingMember.isPresent()) {
            LOGGER.info("Updating existing member in chat with ID: {}", instance.getChatId());
            Members member = existingMember.get();
            member.setRole(instance.getRole());
            member.setCanDeleteMessages(instance.isCanDeleteMessages());
            member.setCanAddParticipants(instance.isCanAddParticipants());
            member.setCanEditMessages(instance.isCanEditMessages());
            member.setCaret(instance.getCaret());
            member.setJoinedAt(instance.getJoinedAt());
            return repository.update(member);
        } else {
            Chat chat = chatService.find(instance.getChatId()).orElseThrow(() -> new IllegalArgumentException("Chat not found"));

            if (chat.getType().equals("p2p")) {
                List<Members> members = findAll(new PageRequest(0, Long.MAX_VALUE, new ArrayList<>()), new Members(instance.getChatId(), null, null, false, false, false, null, null));
                if (members.size() >= 2) {
                    throw new IllegalArgumentException("P2P chat can have maximum 2 members");
                }
            }

            instance.setJoinedAt(LocalDateTime.now());
            return repository.save(instance);
        }
    }

    public void delete(String chatId, String userId) {
        LOGGER.info("Removing member from chat with ID: {}", chatId);
        if (chatId == null || chatId.isEmpty()) {
            throw new IllegalArgumentException("Chat ID cannot be null or empty");
        }
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }

        Members member = repository.find(chatId, userId).orElseThrow(() -> new IllegalArgumentException("Member not found"));

        if (member.getRole().equals("creator")) {
            throw new IllegalArgumentException("Creator of the chat cannot be removed");
        }

        repository.delete(chatId, userId);
    }

    public void promoteToAdmin(String chatId, String userId) {
        LOGGER.info("Promoting member to admin in chat with ID: {}", chatId);
        Members member = repository.find(chatId, userId).orElseThrow(() -> new IllegalArgumentException("Member not found"));

        if (member.getRole().equals("creator")) {
            throw new IllegalArgumentException("Creator cannot be promoted to admin");
        }

        member.setRole("admin");
        member.setCanDeleteMessages(true);
        member.setCanAddParticipants(true);
        member.setCanEditMessages(true);
        repository.update(member);
    }

    public void demoteToMember(String chatId, String userId) {
        LOGGER.info("Demoting admin to member in chat with ID: {}", chatId);
        Members member = repository.find(chatId, userId).orElseThrow(() -> new IllegalArgumentException("Member not found"));

        if (member.getRole().equals("creator")) {
            throw new IllegalArgumentException("Creator cannot be demoted to member");
        }

        member.setRole("member");
        member.setCanDeleteMessages(false);
        member.setCanAddParticipants(false);
        member.setCanEditMessages(false);
        repository.update(member);
    }

    public void promoteToRedactor(String chatId, String userId) {
        LOGGER.info("Promoting member to redactor in chat with ID: {}", chatId);
        Members member = repository.find(chatId, userId).orElseThrow(() -> new IllegalArgumentException("Member not found"));

        if (member.getRole().equals("creator")) {
            throw new IllegalArgumentException("Creator cannot be promoted to redactor");
        }

        member.setRole("redactor");
        member.setCanEditMessages(true);
        repository.update(member);
    }
}
