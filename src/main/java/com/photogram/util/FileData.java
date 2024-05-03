package com.photogram.util;

import lombok.Getter;

import java.io.InputStream;
@Getter
public class FileData {
    private final String fileName;
    private final InputStream fileStream;

    public FileData(String fileName, InputStream fileStream) {
        this.fileName = fileName;
        this.fileStream = fileStream;
    }

}
