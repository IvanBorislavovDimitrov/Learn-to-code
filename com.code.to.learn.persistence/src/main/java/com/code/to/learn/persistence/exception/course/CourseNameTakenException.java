package com.code.to.learn.persistence.exception.course;

import com.code.to.learn.persistence.constant.Messages;

public class CourseNameTakenException extends CourseException {

    public CourseNameTakenException(String courseName) {
        super(Messages.COURSE_NAME_IS_TAKEN, courseName);
    }
}
