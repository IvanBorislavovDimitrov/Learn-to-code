package com.code.to.learn.core.exception.user;

import com.code.to.learn.core.constant.Messages;

import java.text.MessageFormat;

public class EmailTakenException extends UserException {

    public EmailTakenException(String username) {
        super(MessageFormat.format(Messages.EMAIL_IS_TAKEN, username));
    }
}
