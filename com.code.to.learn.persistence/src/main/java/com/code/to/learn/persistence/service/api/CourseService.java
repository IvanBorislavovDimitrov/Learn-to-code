package com.code.to.learn.persistence.service.api;

import com.code.to.learn.persistence.domain.entity.Course;
import com.code.to.learn.persistence.domain.model.CourseServiceModel;

import java.util.List;

public interface CourseService extends GenericService<CourseServiceModel>, NamedElementService<Course, CourseServiceModel> {

    boolean isNameTaken(String courseName);

    List<CourseServiceModel> findLatestCourses(int count);

    List<CourseServiceModel> findCourses(int page, int maxResults, String name, String category);

    long countByNameLike(String name);

    CourseServiceModel enrollUserForCourse(String username, String courseName);

    CourseServiceModel addToCart(String username, String courseName);

    List<CourseServiceModel> getCoursesInCart(String username);

}
