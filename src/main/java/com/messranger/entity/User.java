package com.messranger.entity;
import java.util.*;
import java.net.*;
import java.io.*;

public class User{
    private String id = UUID.randomUUID().toString();
    private String nickname;
    private String phoneNumber;
    private boolean status;
    private boolean verifiedAccount;

    //private ServerSocket socket;
    private AttachedItems profilePhoto;

    public User(String nickname, String phoneNumber) {
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
    }

    public String getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public boolean isStatus() {
        return status;
    }

    public boolean isVerifiedAccount() {
        return verifiedAccount;
    }

    public AttachedItems getProfilePhoto() {
        return profilePhoto;
    }
}
