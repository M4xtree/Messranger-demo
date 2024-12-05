package com.messranger.services;

import com.messranger.entity.User;
import com.messranger.model.PageRequest;
import com.messranger.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class UserService extends BaseService<User> {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    public UserService() {
        repository = new UserRepository(dbConfig.getDataSource());
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
    public void delete(String id) {
        LOGGER.info("Deleting user with ID: {}", id);
        if (!repository.find(id).isEmpty()) {
            repository.delete(id);
        }
    }
}