package com.code.to.learn.persistence.exception.basic;

public class InvalidTokenException extends LCException {

    public InvalidTokenException(String message, Object... args) {
        super(message, args);
    }
}
