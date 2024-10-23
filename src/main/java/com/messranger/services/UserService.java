package com.messranger.services;

import com.messranger.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User create(String nickname,String phoneNumber);
    User update(String id,String nickname, String phoneNumber);
    Optional<User> getById(String id);
    void delete(String id);
    List<User> getAll();
    List<User> search(String nicknameFilter, String phoneNumberFilter);
}
