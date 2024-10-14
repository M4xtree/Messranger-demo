package com.messranger.entity;

import java.util.UUID;

public class Identifier {
    private String id;

    public Identifier() {
            id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

}
