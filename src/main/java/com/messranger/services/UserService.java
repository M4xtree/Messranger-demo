package com.messranger.services;

import com.messranger.entity.User;
import com.messranger.repositories.UserRepository;



public class UserService extends BaseService<User>{
    public UserService() {
        repository = new UserRepository(dbConfig.getDataSource());
    }

    @Override
    public User update(User instance) {
        User repUser = repository.find(instance.getId()).orElseThrow();
        repUser.setNickname(instance.getNickname());
        repUser.setPhoneNumber(instance.getPhoneNumber());
        return repository.update(repUser);
    }
}
