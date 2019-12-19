package com.code.to.learn.core.exception.user;

import com.code.to.learn.core.constant.Messages;

public class PasswordsNotMatchException extends UserException {

    public PasswordsNotMatchException() {
        super(Messages.PASSWORDS_DO_NOT_MATCH);
    }
}
