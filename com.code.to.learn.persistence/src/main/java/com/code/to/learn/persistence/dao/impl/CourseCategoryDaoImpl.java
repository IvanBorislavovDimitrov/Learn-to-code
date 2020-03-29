package com.code.to.learn.persistence.dao.impl;

import com.code.to.learn.persistence.dao.api.CourseCategoryDao;
import com.code.to.learn.persistence.domain.entity.CourseCategory;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CourseCategoryDaoImpl extends GenericDaoImpl<CourseCategory> implements CourseCategoryDao {

    @Autowired
    public CourseCategoryDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    protected Class<CourseCategory> getDomainClassType() {
        return CourseCategory.class;
    }

    @Override
    public Optional<CourseCategory> findByName(String courseName) {
        return findByField(CourseCategory.NAME, courseName);
    }
}
