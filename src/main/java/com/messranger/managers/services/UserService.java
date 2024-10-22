package com.messranger.managers.services;

import com.messranger.entity.User;
import com.messranger.managers.repositories.UserRepository;

public class UserService extends BaseService<User> {

    public UserService(UserRepository userRepository) {
        super(userRepository);
    }
}