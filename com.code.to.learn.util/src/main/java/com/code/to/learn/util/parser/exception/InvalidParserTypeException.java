package com.code.to.learn.util.parser.exception;

public class InvalidParserTypeException extends RuntimeException {

    private static final String INVALID_PARSER_TYPE = "Invalid parser type";

    public InvalidParserTypeException() {
        super(INVALID_PARSER_TYPE);
    }
}
