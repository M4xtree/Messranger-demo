package com.messranger.entity;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.UUID;

public class Message {
    private String id = UUID.randomUUID().toString();
    private String message;
    private ArrayList<AttachedItems> attachments;
    private LocalDateTime creationDate = LocalDateTime.now();

    public Message(String message, ArrayList<AttachedItems> attachments) {
        this.message = message;
        this.attachments = attachments;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<AttachedItems> getAttachments() {
        return attachments;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }
}
