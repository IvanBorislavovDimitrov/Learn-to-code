package com.code.to.learn.persistence.exception.course;

import com.code.to.learn.persistence.constant.Messages;

import java.time.LocalDate;

public class InvalidDatesException extends CourseException {

    public InvalidDatesException(LocalDate startDate, LocalDate endDate) {
        super(Messages.INVALID_DATES, startDate, endDate);
    }
}
