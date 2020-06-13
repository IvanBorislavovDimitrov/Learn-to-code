package com.code.to.learn.core.validator;

import com.code.to.learn.api.model.course.CourseBindingModel;
import com.code.to.learn.persistence.exception.course.CourseNameTakenException;
import com.code.to.learn.persistence.service.api.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CourseValidator {

    private final CourseService courseService;

    @Autowired
    public CourseValidator(CourseService courseService) {
        this.courseService = courseService;
    }

    public void validateCourseBindingModel(CourseBindingModel courseBindingModel) {
        validateCourseName(courseBindingModel.getName());
    }

    private void validateCourseName(String courseName) {
        if (courseService.isNameTaken(courseName)) {
            throw new CourseNameTakenException(courseName);
        }
    }

}
