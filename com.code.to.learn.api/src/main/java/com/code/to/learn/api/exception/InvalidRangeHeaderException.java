package com.code.to.learn.api.exception;

import java.text.MessageFormat;

public class InvalidRangeHeaderException extends HeaderException {

    private static final String INVALID_RANGE_HEADER = "Invalid range header: {0}";

    public InvalidRangeHeaderException() {
        this("");
    }

    public InvalidRangeHeaderException(String message) {
        super(MessageFormat.format(INVALID_RANGE_HEADER, message));
    }
}
