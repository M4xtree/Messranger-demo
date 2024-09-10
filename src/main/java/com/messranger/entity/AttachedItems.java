package com.messranger.entity;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AttachedItems{
    private String fileName;
    private Path filePath;

    public AttachedItems(String fileName){
        this.fileName = fileName;
        this.filePath = Paths.get(fileName);
    }

}
