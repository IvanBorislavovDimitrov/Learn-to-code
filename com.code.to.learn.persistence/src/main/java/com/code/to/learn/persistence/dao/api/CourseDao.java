package com.code.to.learn.persistence.dao.api;

import com.code.to.learn.persistence.domain.entity.Course;

import java.util.List;
import java.util.Optional;

public interface CourseDao extends GenericDao<Course> {

    Optional<Course> findByName(String courseName);

    List<Course> findLatestCourses(int count);

    List<Course> findCourses(int page, int maxResults, String name, String category);

    long countByNameLike(String name);

    List<Course> findBestSellers(int limit);

    List<Course> findMostCommented(int limit);

    Course markCourseAsRatedByUser(String courseName, String username);

    boolean hasUserRatedCourse(String courseName, String username);
}
