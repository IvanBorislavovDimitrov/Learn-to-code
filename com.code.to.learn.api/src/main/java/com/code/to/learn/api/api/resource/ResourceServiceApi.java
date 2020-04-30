package com.code.to.learn.api.api.resource;

import java.io.InputStream;

public interface ResourceServiceApi {

    byte[] getImageResource(String name);

    InputStream openFileStream(String name, Long offset);
}
