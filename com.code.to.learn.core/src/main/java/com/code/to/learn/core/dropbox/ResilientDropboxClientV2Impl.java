package com.code.to.learn.core.dropbox;

import com.code.to.learn.core.util.ResilientExecutor;
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
public class ResilientDropboxClientV2Impl implements DropboxClient {

    private final DbxClientV2 client;
    private final ResilientExecutor resilientExecutor;

    @Autowired
    public ResilientDropboxClientV2Impl(DropboxClientFactory dropboxClientFactory, ResilientExecutor resilientExecutor) {
        client = dropboxClientFactory.createDropboxClient();
        this.resilientExecutor = resilientExecutor;
    }

    @Override
    public FullAccount getCurrentAccount() {
        return resilientExecutor.executeWithRetry(() -> client.users().getCurrentAccount());
    }

    @Override
    public FileMetadata uploadFile(InputStream inputStream, String filename) {
        return resilientExecutor.executeWithRetry(() -> client.files().uploadBuilder(insertFrontSlash(filename))
                .withMode(WriteMode.OVERWRITE)
                .uploadAndFinish(inputStream));
    }

    @Override
    public FileMetadata getFile(String filename, File file) {
        try {
            DbxDownloader<FileMetadata> fileToDownload = client.files().download(insertFrontSlash(filename));
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            return resilientExecutor.executeWithRetry(() -> fileToDownload.download(fileOutputStream));
        } catch (DbxException | IOException e) {
            throw new LCException(e.getMessage(), e);
        }
    }

    @Override
    public InputStream getFileAsInputStream(String filename, Long offset) {
        return resilientExecutor.executeWithRetry(() -> client.files()
                .downloadBuilder(insertFrontSlash(filename))
                .range(offset)
                .start()
                .getInputStream());
    }

    @Override
    public void removeFile(String filename) {
        resilientExecutor.executeWithRetry(() -> client.files().deleteV2(insertFrontSlash(filename)));
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
