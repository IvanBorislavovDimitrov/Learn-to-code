package com.code.to.learn.web.util;

import org.springframework.web.multipart.MultipartFile;

public class FileToUpload {

    private final String filename;
    private final MultipartFile file;

    public FileToUpload(String filename, MultipartFile file) {
        this.filename = filename;
        this.file = file;
    }

    public String getFilename() {
        return filename;
    }

    public MultipartFile getFile() {
        return file;
    }
}
