package com.messranger.services;

import com.messranger.entity.Members;

import java.util.List;
import java.util.Optional;

public interface MembersService {
    Members add(String chatId, String userId, String role);
    Members update(String chatId, String userId, String role);
    Optional<Members> get(String chatId, String userId);
    void remove(String chatId, String userId);
    List<Members> getAll();
    List<Members> search();
}
