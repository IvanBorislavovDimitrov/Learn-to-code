package com.code.to.learn.persistence.exception.user;

import com.code.to.learn.persistence.constant.Messages;

public class EmailTakenException extends UserException {

    public EmailTakenException(String email) {
        super(Messages.EMAIL_IS_TAKEN, email);
    }
}
