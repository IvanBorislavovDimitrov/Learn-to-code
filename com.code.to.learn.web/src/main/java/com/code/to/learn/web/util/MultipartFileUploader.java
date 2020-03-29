package com.code.to.learn.web.util;

import com.code.to.learn.core.dropbox.DropboxClient;
import com.code.to.learn.persistence.exception.basic.LCException;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Component
public class MultipartFileUploader {

    private final DropboxClient dropboxClient;

    @Autowired
    public MultipartFileUploader(DropboxClient dropboxClient) {
        this.dropboxClient = dropboxClient;
    }

    public void uploadFile(MultipartFile multipartFile, String filename) {
        InputStream inputStream = getInputStream(multipartFile);
        File file = null;
        try {
            file = new File(filename);
            FileUtils.copyInputStreamToFile(inputStream, file);
            dropboxClient.uploadFile(file);
        } catch (IOException e) {
            throw new LCException(e.getMessage(), e);
        } finally {
            FileUtils.deleteQuietly(file);
        }
    }

    private InputStream getInputStream(MultipartFile multipartFile) {
        try {
            return multipartFile.getInputStream();
        } catch (IOException e) {
            throw new LCException(e.getMessage(), e);
        }
    }
}
