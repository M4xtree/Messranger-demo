package com.messranger.managers.controllers;

import com.messranger.entity.Members;
import com.messranger.managers.model.PageRequest;
import com.messranger.managers.services.MembersService;

import java.util.List;
import java.util.Optional;

public class MembersController {

    private final MembersService membersService;

    public MembersController(MembersService membersService) {
        this.membersService = membersService;
    }

    public Members addMember(String chatId, String userId, String role) {
        Members member = new Members(chatId, userId, role, false, false, false, "", null);
        return membersService.save(member);
    }

    public Members updateMember(String chatId, String userId, String role) {
        Optional<Members> existingMember = membersService.find(userId);
        if (existingMember.isPresent()) {
            Members member = existingMember.get();
            member.setRole(role);
            return membersService.update(member);
        }
        return null;
    }

    public Optional<Members> getMember(String chatId, String userId) {
        return membersService.find(userId);
    }

    public void removeMember(String chatId, String userId) {
        membersService.delete(userId);
    }

    public List<Members> getAllMembers(int limit, long offset) {
        PageRequest pageRequest = new PageRequest(limit, offset, List.of("joined_at DESC"));
        return membersService.findAll(pageRequest);
    }

    public List<Members> searchMembers(String roleFilter, int limit, long offset) {
        Members filter = new Members(null, null, roleFilter, false, false, false, "", null);
        PageRequest pageRequest = new PageRequest(limit, offset, List.of("role ASC"));
        return membersService.findAll(pageRequest, filter);
    }
}
