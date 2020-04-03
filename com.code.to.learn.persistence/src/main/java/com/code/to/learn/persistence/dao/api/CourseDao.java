package com.code.to.learn.persistence.dao.api;

import com.code.to.learn.persistence.domain.entity.Course;

import java.util.List;
import java.util.Optional;

public interface CourseDao extends GenericDao<Course> {

    Optional<Course> findByName(String courseName);

    List<Course> findLatestCourses(int count);

    List<Course> findCoursesByPage(int page, int maxResults);
}
