package com.code.to.learn.web.api_impl;

import com.code.to.learn.api.api.resource.ResourceServiceApi;
import com.code.to.learn.core.dropbox.DropboxClient;
import com.code.to.learn.persistence.exception.basic.LCException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.util.UUID;

@Component
public class ResourceServiceApiImpl implements ResourceServiceApi {

    private final DropboxClient dropboxClient;

    @Autowired
    public ResourceServiceApiImpl(DropboxClient dropboxClient) {
        this.dropboxClient = dropboxClient;
    }

    @Override
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
}
