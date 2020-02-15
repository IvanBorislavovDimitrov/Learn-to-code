package com.code.to.learn.persistence.dao.impl;

import com.code.to.learn.persistence.domain.entity.Course;
import com.code.to.learn.persistence.dao.api.CourseDao;
import org.springframework.stereotype.Repository;

@Repository
public class CourseDaoImpl extends GenericDaoImpl<Course> implements CourseDao {

    @Override
    protected Class<Course> getDomainClassType() {
        return Course.class;
    }
}
