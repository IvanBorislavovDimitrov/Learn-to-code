package com.code.to.learn.util.parser;

import com.code.to.learn.util.parser.exception.ParsingException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public abstract class Parser {

    private final ObjectMapper objectMapper = createMapper();

    public <T> String serialize(T type) {
        try {
            return objectMapper.writeValueAsString(type);
        } catch (JsonProcessingException e) {
            throw new ParsingException(e.getMessage(), e);
        }
    }

    public <T> T deserialize(String objectValue, Class<T> clazz) {
        try {
            return objectMapper.readValue(objectValue, clazz);
        } catch (IOException e) {
            throw new ParsingException(e.getMessage(), e);
        }
    }

    public <T> T deserialize(String objectValue, TypeReference<T> typeReference) {
        try {
            return objectMapper.readValue(objectValue, typeReference);
        } catch (IOException e) {
            throw new ParsingException(e.getMessage(), e);
        }
    }

    protected abstract ObjectMapper createMapper();
}
