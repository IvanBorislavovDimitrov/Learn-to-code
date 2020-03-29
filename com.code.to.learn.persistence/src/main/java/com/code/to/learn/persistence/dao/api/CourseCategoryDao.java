package com.code.to.learn.persistence.dao.api;

import com.code.to.learn.persistence.domain.entity.CourseCategory;

import java.util.Optional;

public interface CourseCategoryDao extends GenericDao<CourseCategory> {

    Optional<CourseCategory> findByName(String courseCategoryName);
}
