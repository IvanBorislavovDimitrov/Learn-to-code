package com.code.to.learn.web.util;

import com.code.to.learn.core.dropbox.DropboxClient;
import com.code.to.learn.persistence.exception.basic.LCException;
import com.dropbox.core.v2.files.FileMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

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

    public List<FileMetadata> uploadFilesAsync(List<FileToUpload> filesToUpload) {
        List<Future<FileMetadata>> futureFilesToUpload = new ArrayList<>();
        filesToUpload.forEach(fileToUpload -> futureFilesToUpload.add(uploadFileAsync(fileToUpload)));
        return futureFilesToUpload.stream().map(futureFileToUpload -> {
            try {
                return futureFileToUpload.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new LCException(e.getMessage(), e);
            }
        }).collect(Collectors.toList());
    }

    public Future<FileMetadata> uploadFileAsync(FileToUpload fileToUpload) {
        return executorService.submit(() -> uploadFileSync(fileToUpload));
    }

    public FileMetadata uploadFileSync(FileToUpload fileToUpload) {
        try {
            LOGGER.info(MessageFormat.format("Uploading file to Dropbox: {0}", fileToUpload.getFilename()));
            return dropboxClient.uploadFile(fileToUpload.getFile().getInputStream(), fileToUpload.getFilename());
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
