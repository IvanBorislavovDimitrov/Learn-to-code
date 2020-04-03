package com.code.to.learn.persistence.service.api;

import com.code.to.learn.persistence.domain.entity.Course;
import com.code.to.learn.persistence.domain.model.CourseServiceModel;

import java.util.List;

public interface CourseService extends GenericService<CourseServiceModel>, NamedElementService<Course, CourseServiceModel> {

    boolean isNameTaken(String courseName);

    List<CourseServiceModel> findLatestCourses(int count);

    List<CourseServiceModel> findCoursesByPage(int page, int maxResults);
}
