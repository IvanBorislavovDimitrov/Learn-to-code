package com.code.to.learn.api.exception;

public abstract class HeaderException extends RuntimeException {

    public HeaderException() {
    }

    public HeaderException(String message) {
        super(message);
    }
}
