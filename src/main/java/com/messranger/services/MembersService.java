package com.messranger.services;

import com.messranger.entity.Members;
import com.messranger.repositories.MembersRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public class MembersService extends BaseService<Members>{
    public MembersService() {
        repository = new MembersRepository(dbConfig.getDataSource());
    }

    @Override
    public Members update(Members instance) {
        Members member = repository.find(instance.getChatId(),instance.getUserId()).get();
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
}
