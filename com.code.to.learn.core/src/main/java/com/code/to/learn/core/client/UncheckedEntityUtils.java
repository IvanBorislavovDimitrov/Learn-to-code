package com.code.to.learn.core.client;

import com.code.to.learn.persistence.exception.basic.LCException;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public final class UncheckedEntityUtils {

    private UncheckedEntityUtils() {

    }

    public static String getResponseBody(HttpResponse httpResponse) {
        try {
            return EntityUtils.toString(httpResponse.getEntity());
        } catch (IOException e) {
            throw new LCException(e);
        }
    }
}
