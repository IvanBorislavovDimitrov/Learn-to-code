package com.code.to.learn.web.util;

import com.code.to.learn.core.dropbox.DropboxClient;
import com.code.to.learn.persistence.exception.basic.LCException;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class RemoteStorageFileGetter {

    private final DropboxClient dropboxClient;

    @Autowired
    public RemoteStorageFileGetter(DropboxClient dropboxClient) {
        this.dropboxClient = dropboxClient;
    }

    public byte[] getImageResource(String name) {
        try (InputStream fileAsInputStream = dropboxClient.getFileAsInputStream(name, 0L)) {
            return IOUtils.toByteArray(fileAsInputStream);
        } catch (Exception e) {
            throw new LCException(e.getMessage(), e);
        }
    }

    public InputStream getFileStream(String name, Long offset) {
        return dropboxClient.getFileAsInputStream(name, offset);
    }

}
