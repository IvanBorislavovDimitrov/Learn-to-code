package com.code.to.learn.process.exception.general;

import com.code.to.learn.process.constant.Messages;

public class InvalidParserTypeException extends RuntimeException {

    public InvalidParserTypeException() {
        super(Messages.INVALID_PARSER_TYPE);
    }
}
