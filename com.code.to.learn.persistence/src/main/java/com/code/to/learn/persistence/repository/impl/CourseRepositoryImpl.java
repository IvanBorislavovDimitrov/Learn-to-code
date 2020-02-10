package com.code.to.learn.persistence.repository.impl;

import com.code.to.learn.persistence.domain.entity.Course;
import com.code.to.learn.persistence.repository.api.CourseRepository;
import org.springframework.stereotype.Repository;

@Repository
public class CourseRepositoryImpl extends GenericRepositoryImpl<Course> implements CourseRepository {

    @Override
    protected Class<Course> getDomainClassType() {
        return Course.class;
    }
}
