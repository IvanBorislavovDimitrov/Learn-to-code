package com.code.to.learn.api.api.course;

import com.code.to.learn.api.model.course.CourseBindingModel;
import com.code.to.learn.api.model.course.CourseResponseModel;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CourseServiceApi {

    ResponseEntity<CourseResponseModel> addCourse(CourseBindingModel courseBindingModel);

    ResponseEntity<List<CourseResponseModel>> getLatestCourses(int count, boolean loadThumbnails);

    ResponseEntity<List<CourseResponseModel>> getCourses(int page, String name, String category);

}
