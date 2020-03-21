package com.code.to.learn.core.dropbox;

import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.users.FullAccount;

import java.io.File;

public interface DropboxClient {

    FullAccount getCurrentAccount();

    FileMetadata uploadFile(File file);

    FileMetadata getFile(String filename, File path);
}
