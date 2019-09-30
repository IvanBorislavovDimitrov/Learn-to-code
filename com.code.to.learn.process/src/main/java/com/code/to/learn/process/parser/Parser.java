package com.code.to.learn.process.parser;

import com.code.to.learn.process.exception.general.ParsingException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public abstract class Parser {

    private ObjectMapper objectMapper = createMapper();

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

    protected abstract ObjectMapper createMapper();
}
