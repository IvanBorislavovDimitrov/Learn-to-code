package com.code.to.learn.persistence.exception.user;

import com.code.to.learn.persistence.constant.Messages;

public class PasswordsNotMatchException extends UserException {

    public PasswordsNotMatchException() {
        super(Messages.PASSWORDS_DO_NOT_MATCH);
    }
}
