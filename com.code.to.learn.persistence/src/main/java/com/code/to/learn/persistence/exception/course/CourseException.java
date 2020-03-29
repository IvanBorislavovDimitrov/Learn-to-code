package com.code.to.learn.persistence.exception.course;

import com.code.to.learn.persistence.exception.basic.LCException;

public class CourseException extends LCException {

    public CourseException(String message, Object... args) {
        super(message, args);
    }

    public CourseException(String message, Throwable cause) {
        super(message, cause);
    }

}
