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

    @Override
    public User update(User instance) {
        User repUser = repository.find(instance.getId()).get();
        repUser.setNickname(instance.getNickname());
        repUser.setPhoneNumber(instance.getPhoneNumber());
        return repository.update(repUser);
    }
}
