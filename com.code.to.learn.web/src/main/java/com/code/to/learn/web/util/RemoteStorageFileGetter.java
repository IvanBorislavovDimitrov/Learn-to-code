package com.code.to.learn.web.util;

import com.code.to.learn.core.dropbox.DropboxClient;
import com.code.to.learn.persistence.exception.basic.LCException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.UUID;

@Component
public class RemoteStorageFileGetter {

    private final DropboxClient dropboxClient;

    @Autowired
    public RemoteStorageFileGetter(DropboxClient dropboxClient) {
        this.dropboxClient = dropboxClient;
    }

    public byte[] getImageResource(String name) {
        String tempFileName = UUID.randomUUID().toString();
        File file = new File(tempFileName);
        try {
            dropboxClient.getFile(name, file);
            return IOUtils.toByteArray(new FileInputStream(file));
        } catch (Exception e) {
            throw new LCException(e.getMessage(), e);
        } finally {
            FileUtils.deleteQuietly(file);
        }
    }

    public InputStream getFileStream(String name) {
        return dropboxClient.getFileAsInputStream(name);
    }

}