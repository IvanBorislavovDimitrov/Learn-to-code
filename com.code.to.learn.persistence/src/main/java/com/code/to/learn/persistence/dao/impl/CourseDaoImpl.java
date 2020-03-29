package com.code.to.learn.persistence.dao.impl;

import com.code.to.learn.persistence.dao.api.CourseDao;
import com.code.to.learn.persistence.domain.entity.Course;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CourseDaoImpl extends GenericDaoImpl<Course> implements CourseDao {

    @Autowired
    public CourseDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Optional<Course> findByName(String courseName) {
        return findByField(Course.NAME, courseName);
    }

    @Override
    protected Class<Course> getDomainClassType() {
        return Course.class;
    }
}
