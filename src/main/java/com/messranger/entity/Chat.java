package com.messranger.entity;

import java.util.ArrayList;
import java.util.UUID;

public class Chat{
    private String id = UUID.randomUUID().toString();
    private String parentId;
    private String creatorId;
    private String adminId;
    private String membersId;
    private String pinId;
    private String messageId;

    private String name;

    private int countOfMembers;
    //private AttachedItems chatAttachments;

    //id участников,имя,тип,родители,закрепы,админы,группы


    public String getId() {
        return id;
    }

    public String getParentId() {
        return parentId;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public String getAdminId() {
        return adminId;
    }

    public String getMembersId() {
        return membersId;
    }

    public String getPinId() {
        return pinId;
    }

    public String getName() {
        return name;
    }

    public int getCountOfMembers() {
        return countOfMembers;
    }

    public String getMessageId() {
        return messageId;
    }
}
