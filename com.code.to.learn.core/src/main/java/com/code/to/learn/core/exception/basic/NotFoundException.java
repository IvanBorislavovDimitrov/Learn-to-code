package com.code.to.learn.core.exception.basic;

public class NotFoundException extends LCException {

    public NotFoundException(String message, Object... args) {
        super(message, args);
    }
}
