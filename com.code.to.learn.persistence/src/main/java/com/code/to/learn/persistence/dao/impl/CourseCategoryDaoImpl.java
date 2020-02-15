package com.code.to.learn.persistence.dao.impl;

import com.code.to.learn.persistence.domain.entity.CourseCategory;
import com.code.to.learn.persistence.dao.api.CourseCategoryDao;
import org.springframework.stereotype.Repository;

@Repository
public class CourseCategoryDaoImpl extends GenericDaoImpl<CourseCategory> implements CourseCategoryDao {

    @Override
    protected Class<CourseCategory> getDomainClassType() {
        return CourseCategory.class;
    }
}
