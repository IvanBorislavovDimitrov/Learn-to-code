package com.code.to.learn.persistence.repository.impl;

import com.code.to.learn.persistence.domain.entity.CourseCategory;
import com.code.to.learn.persistence.repository.api.CourseCategoryRepository;
import org.springframework.stereotype.Repository;

@Repository
public class CourseCategoryRepositoryImpl extends GenericRepositoryImpl<CourseCategory> implements CourseCategoryRepository {

    @Override
    protected Class<CourseCategory> getDomainClassType() {
        return CourseCategory.class;
    }
}
