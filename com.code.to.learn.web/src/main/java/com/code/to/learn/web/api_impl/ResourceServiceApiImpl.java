package com.code.to.learn.web.api_impl;

import com.code.to.learn.api.api.resource.ResourceServiceApi;
import com.code.to.learn.web.util.FileGetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ResourceServiceApiImpl implements ResourceServiceApi {

    private final FileGetter fileGetter;

    @Autowired
    public ResourceServiceApiImpl(FileGetter fileGetter) {
        this.fileGetter = fileGetter;
    }

    @Override
    public byte[] getImageResource(String name) {
        return fileGetter.getImageResource(name);
    }
}
