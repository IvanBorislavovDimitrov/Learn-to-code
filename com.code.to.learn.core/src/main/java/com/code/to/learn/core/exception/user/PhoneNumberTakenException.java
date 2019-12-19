package com.code.to.learn.core.exception.user;

import com.code.to.learn.core.constant.Messages;

import java.text.MessageFormat;

public class PhoneNumberTakenException extends UserException {

    public PhoneNumberTakenException(String phoneNumber) {
        super(MessageFormat.format(Messages.PHONE_NUMBER_IS_TAKEN, phoneNumber));
    }

}
