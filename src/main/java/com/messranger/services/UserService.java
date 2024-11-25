package com.messranger.services;

import com.messranger.entity.User;
import com.messranger.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class UserService extends BaseService<User>{
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    public UserService() {
        repository = new UserRepository(dbConfig.getDataSource());
    }

    @Override
    public User update(User instance) {
        LOGGER.info("Updating user with ID: {}", instance.getId());
        User repUser = repository.find(instance.getId()).orElseThrow();
        repUser.setId(instance.getId());
        repUser.setNickname(instance.getNickname());
        repUser.setPhoneNumber(instance.getPhoneNumber());
        return repository.update(repUser);
    }

    @Override
    public User save(User instance) {
        LOGGER.info("Registering new user with nickname: {}", instance.getNickname());
        if(repository.find(instance.getId()).isEmpty()) {
            User newUser = new User(instance.getNickname(), instance.getPhoneNumber());
            return repository.save(newUser);
        }
        return null;
    }
}
