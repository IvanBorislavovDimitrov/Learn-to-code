package com.code.to.learn.process.exception.user;

import com.code.to.learn.process.constant.Messages;

public class PasswordsNotMatchException extends UserException {

    public PasswordsNotMatchException() {
        super(Messages.PASSWORDS_DO_NOT_MATCH);
    }
}
