package com.code.to.learn.core.exception.basic;

public class LCException extends RuntimeException {

    public LCException() {
        super();
    }

    public LCException(Exception exception) {
        super(exception);
    }

    public LCException(String message) {
        super(message);
    }

    public LCException(String message, Throwable cause) {
        super(message, cause);
    }
}
