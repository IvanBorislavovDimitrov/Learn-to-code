package com.code.to.learn.persistence.dao.api;

import com.code.to.learn.persistence.domain.entity.Course;

import java.util.Optional;

public interface CourseDao extends GenericDao<Course> {

    Optional<Course> findByName(String courseName);
}
