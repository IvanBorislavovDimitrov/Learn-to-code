package com.code.to.learn.persistence.exception.user;

import com.code.to.learn.persistence.constant.Messages;

public class UsernameTakenException extends UserException {

    public UsernameTakenException(String username) {
        super(Messages.USERNAME_IS_TAKEN, username);
    }
}
