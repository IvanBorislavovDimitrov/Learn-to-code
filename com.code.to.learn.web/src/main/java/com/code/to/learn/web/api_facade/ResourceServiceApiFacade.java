package com.code.to.learn.web.api_facade;

import com.code.to.learn.api.api.resource.ResourceServiceApi;
import com.code.to.learn.web.util.RemoteStorageFileGetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class ResourceServiceApiFacade implements ResourceServiceApi {

    private final RemoteStorageFileGetter remoteStorageFileGetter;

    @Autowired
    public ResourceServiceApiFacade(RemoteStorageFileGetter remoteStorageFileGetter) {
        this.remoteStorageFileGetter = remoteStorageFileGetter;
    }

    @Override
    public byte[] getImageResource(String name) {
        return remoteStorageFileGetter.getImageResource(name);
    }

    @Override
    public InputStream openFileStream(String name, Long offset) {
        return remoteStorageFileGetter.getFileStream(name, offset);
    }
}
