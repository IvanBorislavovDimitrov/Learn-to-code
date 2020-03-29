package com.code.to.learn.api.api.course;

import com.code.to.learn.api.model.course.CourseBindingModel;
import com.code.to.learn.api.model.course.CourseResponseModel;
import org.springframework.http.ResponseEntity;

public interface CourseServiceApi {

    ResponseEntity<CourseResponseModel> addCourse(CourseBindingModel courseBindingModel);

}
