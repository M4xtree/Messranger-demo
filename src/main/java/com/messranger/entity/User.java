package com.messranger.entity;
import javax.print.DocFlavor;
import java.util.*;
import java.net.*;
import java.io.*;

public class User extends Identifier{
    private String nickname;
    private String phoneNumber;
    private boolean status;
    private boolean verifiedAccount;

    public User(String nickname, String phoneNumber) {
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.status = true;
        this.verifiedAccount = true;
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

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setVerifiedAccount(boolean verifiedAccount) {
        this.verifiedAccount = verifiedAccount;
    }
}
