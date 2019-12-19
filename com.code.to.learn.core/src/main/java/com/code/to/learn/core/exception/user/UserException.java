package com.code.to.learn.core.exception.user;

public abstract class UserException extends RuntimeException {

    protected UserException() {
        super();
    }

    protected UserException(String message) {
        super(message);
    }

    protected UserException(String message, Throwable cause) {
        super(message, cause);
    }


}
