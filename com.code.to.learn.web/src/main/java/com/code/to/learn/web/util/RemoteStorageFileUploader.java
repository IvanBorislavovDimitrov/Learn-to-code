package com.code.to.learn.web.util;

import com.code.to.learn.core.dropbox.DropboxClient;
import com.code.to.learn.persistence.exception.basic.LCException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@Component
public class RemoteStorageFileUploader {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteStorageFileUploader.class);

    private final DropboxClient dropboxClient;
    private final ExecutorService executorService;

    @Autowired
    public RemoteStorageFileUploader(DropboxClient dropboxClient, ExecutorService executorService) {
        this.dropboxClient = dropboxClient;
        this.executorService = executorService;
    }

    public List<Future<?>> uploadFilesAsync(List<FileToUpload> filesToUpload) {
        List<Future<?>> futureFilesToUpload = new ArrayList<>();
        filesToUpload.forEach(fileToUpload -> futureFilesToUpload.add(uploadFileAsync(fileToUpload)));
        return futureFilesToUpload;
    }

    public Future<?> uploadFileAsync(FileToUpload fileToUpload) {
        return executorService.submit(() -> uploadFileSync(fileToUpload));
    }

    public void uploadFileSync(FileToUpload fileToUpload) {
        try {
            LOGGER.info(MessageFormat.format("Uploading file to Dropbox: {0}", fileToUpload.getFilename()));
            dropboxClient.uploadFile(fileToUpload.getFile().getInputStream(), fileToUpload.getFilename());
        } catch (IOException e) {
            throw new LCException(e.getMessage(), e);
        }
    }

    public void removeFileSync(String filename) {
        try {
            dropboxClient.removeFile(filename);
        } catch (LCException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
