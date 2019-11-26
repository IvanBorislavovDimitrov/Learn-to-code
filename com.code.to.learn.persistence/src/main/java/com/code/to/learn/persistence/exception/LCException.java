package com.code.to.learn.persistence.exception;

import java.text.MessageFormat;

public abstract class LCException extends RuntimeException {

    protected LCException() {
        super();
    }

    protected LCException(String message) {
        super(message);
    }

    protected LCException(String message, Object... args) {
        super(MessageFormat.format(message, args));
    }

    protected LCException(String message, Throwable throwable) {
        this(throwable, message);
    }

    protected LCException(Throwable throwable, String message, Object... args) {
        super(MessageFormat.format(message, args), throwable);
    }

}
