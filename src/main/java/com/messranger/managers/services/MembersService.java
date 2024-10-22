package com.messranger.managers.services;


import com.messranger.entity.Members;
import com.messranger.managers.repositories.MembersRepository;

public class MembersService extends BaseService<Members>{

    public MembersService(MembersRepository membersRepository) {
        super(membersRepository);
    }
}
