package com.code.to.learn.persistence.exception;

import java.text.MessageFormat;

public abstract class PersistenceExcption extends RuntimeException {

    protected PersistenceExcption() {
        super();
    }

    protected PersistenceExcption(String message) {
        super(message);
    }

    protected PersistenceExcption(String message, Object... args) {
        super(MessageFormat.format(message, args));
    }

    protected PersistenceExcption(String message, Throwable throwable) {
        this(throwable, message);
    }

    protected PersistenceExcption(Throwable throwable, String message, Object... args) {
        super(MessageFormat.format(message, args), throwable);
    }

}
