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
import java.util.List;
import java.util.concurrent.Executor;

@Component
public class MultipartFileUploader {

    private final DropboxClient dropboxClient;
    private final Executor executor;

    @Autowired
    public MultipartFileUploader(DropboxClient dropboxClient, Executor executor) {
        this.dropboxClient = dropboxClient;
        this.executor = executor;
    }

    public void uploadFilesAsync(List<FileToUpload> filesToUpload) {
        filesToUpload.forEach(this::uploadFileAsync);
    }

    public void uploadFileAsync(FileToUpload fileToUpload) {
        executor.execute(() -> uploadFile(fileToUpload));
    }

    private void uploadFile(FileToUpload fileToUpload) {
        InputStream inputStream = getInputStream(fileToUpload.getFile());
        File file = null;
        try {
            file = new File(fileToUpload.getFilename());
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
