package com.messranger.managers.controllers;

import com.messranger.entity.User;
import com.messranger.managers.model.PageRequest;
import com.messranger.managers.services.UserService;

import java.util.List;
import java.util.Optional;

public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public User createUser(String nickname, String phoneNumber) {
        User user = new User(nickname, phoneNumber);
        return userService.save(user);
    }

    public User updateUser(String id, String nickname, String phoneNumber) {
        Optional<User> existingUser = userService.find(id);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setNickname(nickname);
            user.setPhoneNumber(phoneNumber);
            return userService.update(user);
        }
        return null;
    }

    public Optional<User> getUserById(String id) {
        return userService.find(id);
    }

    public void deleteUser(String id) {
        userService.delete(id);
    }

    public List<User> getAllUsers(int limit, long offset) {
        PageRequest pageRequest = new PageRequest(limit, offset, List.of("nickname ASC"));
        return userService.findAll(pageRequest);
    }

    public List<User> searchUsers(String nicknameFilter, String phoneNumberFilter, int limit, long offset) {
        User filter = new User(nicknameFilter, phoneNumberFilter);
        PageRequest pageRequest = new PageRequest(limit, offset, List.of("nickname ASC"));
        return userService.findAll(pageRequest, filter);
    }
}





