package com.messranger.services;

import com.messranger.entity.Members;
import com.messranger.repositories.MembersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;

public class MembersService extends BaseService<Members>{
    private static final Logger LOGGER = LoggerFactory.getLogger(MembersService.class);

    public MembersService() {
        repository = new MembersRepository(dbConfig.getDataSource());
    }

    @Override
    public Members update(Members instance) {
        LOGGER.info("Updating member role in chat with ID: {}", instance.getChatId());
        Members member = repository.find(instance.getChatId(),instance.getUserId()).orElseThrow();
        member.setChatId(instance.getChatId());
        member.setUserId(instance.getUserId());
        member.setRole(instance.getRole());
        member.setCanDeleteMessages(instance.isCanDeleteMessages());
        member.setCanAddParticipants(instance.isCanAddParticipants());
        member.setCanEditMessages(instance.isCanEditMessages());
        member.setCaret(instance.getCaret());
        member.setJoinedAt(LocalDateTime.now());
        return repository.update(member);
    }

    @Override
    public Members save(Members instance) {
        LOGGER.info("Adding member to chat with ID: {}", instance.getChatId());
        if (repository.find(instance.getChatId(), instance.getUserId()).isEmpty()) {
            Members member = new Members(instance.getChatId(), instance.getUserId(), instance.getRole(), false, false, false, null, LocalDateTime.now());
            return repository.save(member);
        }
        return null;
    }

    public Members delete(String chatId, String userId){
        LOGGER.info("Removing member from chat with ID: {}", chatId);
        if(!repository.find(chatId, userId).isEmpty()){
            repository.delete(chatId, userId);
        }
        return null;
    }

}
