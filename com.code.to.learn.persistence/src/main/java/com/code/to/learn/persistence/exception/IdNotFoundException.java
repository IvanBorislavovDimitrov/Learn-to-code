package com.code.to.learn.persistence.exception;

public class IdNotFoundException extends PersistenceExcption {

    private static final String ID_NOT_FOUND = "ID: {0} not found";

    public IdNotFoundException(String id) {
        super(ID_NOT_FOUND, id);
    }
}
