package com.code.to.learn.persistence.exception.user;

import com.code.to.learn.persistence.constant.Messages;

public class PasswordsDoNotMatchException extends UserException {

    public PasswordsDoNotMatchException() {
        super(Messages.PASSWORDS_DO_NOT_MATCH);
    }
}
