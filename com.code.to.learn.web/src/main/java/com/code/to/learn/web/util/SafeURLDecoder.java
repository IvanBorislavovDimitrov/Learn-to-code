package com.code.to.learn.web.util;

import com.code.to.learn.core.exception.basic.LCException;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public final class SafeURLDecoder {

    private SafeURLDecoder() {

    }

    public static Map<String, String> decodeMap(Map<String, String> encodedMap) {
        Map<String, String> decodedMap = new HashMap<>();
        encodedMap.forEach((key, value) -> decodedMap.put(key, decodeValue(value)));
        return decodedMap;
    }

    public static String decodeValue(String encodedValue) {
        try {
            return URLDecoder.decode(encodedValue, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            throw new LCException(e.getMessage(), e);
        }
    }
}
