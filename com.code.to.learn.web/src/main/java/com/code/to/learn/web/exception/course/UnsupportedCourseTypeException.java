package com.code.to.learn.web.exception.course;

import com.code.to.learn.persistence.exception.basic.LCException;

public class UnsupportedCourseTypeException extends LCException {

    public UnsupportedCourseTypeException(String message, Object... args) {
        super(message, args);
    }
}
