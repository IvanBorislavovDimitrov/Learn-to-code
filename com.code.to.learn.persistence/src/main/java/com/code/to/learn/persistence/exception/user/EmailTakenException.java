package com.code.to.learn.persistence.exception.user;

import com.code.to.learn.persistence.constant.Messages;

import java.text.MessageFormat;

public class EmailTakenException extends UserException {

    public EmailTakenException(String username) {
        super(MessageFormat.format(Messages.EMAIL_IS_TAKEN, username));
    }
}
