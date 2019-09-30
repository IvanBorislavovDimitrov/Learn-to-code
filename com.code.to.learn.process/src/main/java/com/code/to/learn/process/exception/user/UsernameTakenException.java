package com.code.to.learn.process.exception.user;

import com.code.to.learn.process.constant.Messages;

import java.text.MessageFormat;

public class UsernameTakenException extends UserException {

    public UsernameTakenException(String username) {
        super(MessageFormat.format(Messages.USERNAME_IS_TAKEN, username));
    }
}
