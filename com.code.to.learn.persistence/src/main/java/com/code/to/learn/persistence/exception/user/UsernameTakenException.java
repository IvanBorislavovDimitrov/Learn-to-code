package com.code.to.learn.persistence.exception.user;

import com.code.to.learn.persistence.constant.Messages;

import java.text.MessageFormat;

public class UsernameTakenException extends UserException {

    public UsernameTakenException(String username) {
        super(MessageFormat.format(Messages.USERNAME_IS_TAKEN, username));
    }
}
