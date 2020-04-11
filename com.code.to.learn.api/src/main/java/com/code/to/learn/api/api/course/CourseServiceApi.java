package com.code.to.learn.api.api.course;

import com.code.to.learn.api.model.course.CourseBindingModel;
import com.code.to.learn.api.model.course.CoursePagesResponseModel;
import com.code.to.learn.api.model.course.CourseResponseModel;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CourseServiceApi {

    ResponseEntity<CourseResponseModel> add(CourseBindingModel courseBindingModel);

    ResponseEntity<List<CourseResponseModel>> getLatest(int count, boolean loadThumbnails);

    ResponseEntity<List<CourseResponseModel>> getByPageNameCategory(int page, String name, String category);

    ResponseEntity<CoursePagesResponseModel> getPagesCount(String courseName);

    ResponseEntity<CourseResponseModel> get(String name);

}
