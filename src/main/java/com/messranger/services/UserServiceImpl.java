package com.messranger.services;

import com.messranger.config.DataBaseConfig;
import com.messranger.entity.User;
import com.messranger.model.PageRequest;
import com.messranger.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService{
    private final UserRepository repository;

    private static final DataBaseConfig dbConfig = new DataBaseConfig();

    public UserServiceImpl() {
        repository = new UserRepository(dbConfig.getDataSource());
    }

    @Override
    public User create(String nickname, String phoneNumber) {
        User user = new User(nickname, phoneNumber);
        return repository.save(user);
    }

    @Override
    public User update(String id, String nickname, String phoneNumber) {
        Optional<User> existingUser = repository.find(id);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setNickname(nickname);
            user.setPhoneNumber(phoneNumber);
            return repository.update(user);
        }
        return null;
    }

    @Override
    public Optional<User> getById(String id) {
        return repository.find(id);
    }

    @Override
    public void delete(String id) {
        repository.delete(id);
    }

    @Override
    public List<User> getAll(Integer limit, Long offset) {
        PageRequest pageRequest = new PageRequest(limit, offset, List.of("nickname ASC"));
        return repository.findAll(pageRequest);
    }

    @Override
    public List<User> search(String nicknameFilter, String phoneNumberFilter, Integer limit, Long offset) {
        User filter = new User(nicknameFilter, phoneNumberFilter);
        PageRequest pageRequest = new PageRequest(limit, offset, List.of("nickname ASC"));
        return repository.findAll(pageRequest, filter);
    }
}
