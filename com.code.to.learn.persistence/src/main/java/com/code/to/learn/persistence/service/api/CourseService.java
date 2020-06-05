package com.code.to.learn.persistence.service.api;

import com.code.to.learn.persistence.domain.entity.Course;
import com.code.to.learn.persistence.domain.entity.User;
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

    CourseServiceModel removeCourseFromCart(String username, String courseName);

    void emptyCart(String username);

    void removeFromCart(User user, String courseName);

    List<CourseServiceModel> findBestSellers(int limit);

    List<CourseServiceModel> findMostCommented(int limit);

    CourseServiceModel markCourseAsRatedByUser(String courseName, String username);

    boolean hasUserRatedCourse(String courseName, String username);
}
