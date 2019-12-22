package com.code.to.learn.core.exception.basic;

import java.text.MessageFormat;

public class LCException extends RuntimeException {

    public LCException() {
        super();
    }

    public LCException(Exception exception) {
        super(exception);
    }

    public LCException(String message, Object... args) {
        super(MessageFormat.format(message, args));
    }

    public LCException(String message, Throwable cause) {
        super(message, cause);
    }
}
