package com.messranger.services;

import com.messranger.entity.User;
import com.messranger.model.PageRequest;
import com.messranger.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

public class UserService extends BaseService<User>{
    public UserService() {
        repository = new UserRepository(dbConfig.getDataSource());
    }
    public User create(String nickname, String phoneNumber) {
        User user = new User(nickname, phoneNumber);
        return repository.save(user);
    }
    public User update(String id, String nickname, String phoneNumber) {
        User user = repository.find(id).get();
        user.setNickname(nickname);
        user.setPhoneNumber(phoneNumber);
        return repository.update(user);
    }

    @Override
    public User save(User instance) {
        return create(instance.getNickname(),instance.getPhoneNumber());
    }

    @Override
    public User update(User instance) {
        return update(instance.getId(),instance.getNickname(), instance.getPhoneNumber());
    }
}
