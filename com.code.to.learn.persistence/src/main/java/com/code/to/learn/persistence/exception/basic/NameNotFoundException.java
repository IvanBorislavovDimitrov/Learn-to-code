package com.code.to.learn.persistence.exception.basic;

public class NameNotFoundException extends NotFoundException {

    private static final String NAME_NOT_FOUND = "Name: {0} not found";

    public NameNotFoundException(String id) {
        super(NAME_NOT_FOUND, id);
    }

}
