package com.code.to.learn.persistence.exception.user;

import com.code.to.learn.persistence.constant.Messages;

public class PhoneNumberTakenException extends UserException {

    public PhoneNumberTakenException(String phoneNumber) {
        super(Messages.PHONE_NUMBER_IS_TAKEN, phoneNumber);
    }
}
