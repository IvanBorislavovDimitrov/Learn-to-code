package com.code.to.learn.core.dropbox;

import com.code.to.learn.persistence.exception.basic.LCException;
import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.users.FullAccount;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;

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
    public FileMetadata uploadFile(File file) {
        String absoluteFilePath = file.getAbsolutePath();
        String filename = FilenameUtils.getName(absoluteFilePath);
        try (InputStream inputStream = new FileInputStream(absoluteFilePath)) {
            return client.files().uploadBuilder(insertFrontSlash(filename))
                    .uploadAndFinish(inputStream);
        } catch (IOException | DbxException e) {
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

    @Override
    public FileMetadata getFile(String filename, File path) {
        try {
            DbxDownloader<FileMetadata> fileToDownload = client.files().download(insertFrontSlash(filename));
            FileOutputStream fileOutputStream = new FileOutputStream(path);
            return fileToDownload.download(fileOutputStream);
        } catch (DbxException | IOException e) {
            throw new LCException(e.getMessage(), e);
        }
    }
}
