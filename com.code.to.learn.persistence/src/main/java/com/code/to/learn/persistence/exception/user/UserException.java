package com.code.to.learn.persistence.exception.user;

import com.code.to.learn.persistence.exception.basic.LCException;

public class UserException extends LCException {

    public UserException(String message, Object... args) {
        super(message, args);
    }

    public UserException(String message, Throwable cause) {
        super(message, cause);
    }


}
