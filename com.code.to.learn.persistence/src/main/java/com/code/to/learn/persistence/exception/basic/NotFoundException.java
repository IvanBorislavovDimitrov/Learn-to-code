package com.code.to.learn.persistence.exception.basic;

public class NotFoundException extends LCException {

    public NotFoundException(String message, Object... args) {
        super(message, args);
    }
}
