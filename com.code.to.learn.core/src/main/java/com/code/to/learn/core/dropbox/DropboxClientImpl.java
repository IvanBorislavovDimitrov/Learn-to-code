package com.code.to.learn.core.dropbox;

import com.code.to.learn.persistence.exception.basic.LCException;
import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.WriteMode;
import com.dropbox.core.v2.users.FullAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Component
public class DropboxClientImpl implements DropboxClient {

    private final DbxClientV2 client;

    @Autowired
    public DropboxClientImpl(DropboxClientFactory dropboxClientFactory) {
        client = dropboxClientFactory.createDropboxClient();
    }

    @Override
    public FullAccount getCurrentAccount() {
        try {
            return client.users().getCurrentAccount();
        } catch (DbxException e) {
            throw new LCException(e.getMessage(), e);
        }
    }

    @Override
    public FileMetadata uploadFile(InputStream inputStream, String filename) {
        try {
            return client.files().uploadBuilder(insertFrontSlash(filename))
                    .withMode(WriteMode.OVERWRITE)
                    .uploadAndFinish(inputStream);
        } catch (IOException | DbxException e) {
            throw new LCException(e.getMessage(), e);
        }
    }

    @Override
    public FileMetadata getFile(String filename, File file) {
        try {
            DbxDownloader<FileMetadata> fileToDownload = client.files().download(insertFrontSlash(filename));
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            return fileToDownload.download(fileOutputStream);
        } catch (DbxException | IOException e) {
            throw new LCException(e.getMessage(), e);
        }
    }

    @Override
    public InputStream getFileAsInputStream(String filename) {
        try {
            return client.files().download(insertFrontSlash(filename)).getInputStream();
        } catch (DbxException e) {
            throw new LCException(e.getMessage(), e);
        }
    }

    private String insertFrontSlash(String filename) {
        if (filename == null) {
            return null;
        }
        if (filename.startsWith("/")) {
            return filename;
        }
        return "/" + filename;
    }

}
