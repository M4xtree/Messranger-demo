package com.messranger.services;

import com.messranger.config.DataBaseConfig;
import com.messranger.entity.Members;
import com.messranger.model.PageRequest;
import com.messranger.repositories.MembersRepository;

import java.util.List;
import java.util.Optional;

public class MembersServiceImpl implements MembersService{
    private final MembersRepository repository;
    private PageRequest pageRequest;

    private static final DataBaseConfig dbConfig = new DataBaseConfig();

    public MembersServiceImpl() {
        repository = new MembersRepository(dbConfig.getDataSource());
        pageRequest = new PageRequest(10, 0L, List.of("nickname ASC"));
    }

    @Override
    public Members add(String chatId, String userId, String role) {
        Members member = new Members(chatId, userId, role, false, false, false, "", null);
        return repository.save(member);
    }

    @Override
    public Members update(String chatId, String userId, String role) {
        Optional<Members> existingMember = repository.find(userId);
        if (existingMember.isPresent()) {
            Members member = existingMember.get();
            member.setRole(role);
            return repository.update(member);
        }
        return null;
    }

    @Override
    public Optional<Members> get(String chatId, String userId) {
        return repository.find(userId);

    }

    @Override
    public void remove(String chatId, String userId) {
        repository.delete(userId);
    }

    @Override
    public List<Members> getAll() {
        return repository.findAll(pageRequest);
    }

    @Override
    public List<Members> search() {
        Members filter = new Members(null, null, "", false, false, false, "", null);
        return repository.findAll(pageRequest, filter);
    }
    public PageRequest getPageRequest() {
        return pageRequest;
    }

    public void setPageRequest(PageRequest pageRequest) {
        this.pageRequest = pageRequest;
    }
}
