package com.code.to.learn.core.dropbox;

import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.users.FullAccount;

import java.io.File;
import java.io.InputStream;

public interface DropboxClient {

    FullAccount getCurrentAccount();

    FileMetadata uploadFile(InputStream inputStream, String filename);

    FileMetadata getFile(String filename, File file);

    InputStream getFileAsInputStream(String filename);
}
