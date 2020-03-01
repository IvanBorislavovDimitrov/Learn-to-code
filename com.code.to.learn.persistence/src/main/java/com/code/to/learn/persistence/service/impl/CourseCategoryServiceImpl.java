package com.code.to.learn.persistence.service.impl;

import com.code.to.learn.persistence.dao.api.CourseCategoryDao;
import com.code.to.learn.persistence.domain.entity.CourseCategory;
import com.code.to.learn.persistence.domain.model.CourseCategoryServiceModel;
import com.code.to.learn.persistence.service.api.CourseCategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseCategoryServiceImpl extends GenericServiceImpl<CourseCategory, CourseCategoryServiceModel> implements CourseCategoryService {

    @Autowired
    protected CourseCategoryServiceImpl(CourseCategoryDao courseCategoryDao, ModelMapper modelMapper) {
        super(courseCategoryDao, modelMapper);
    }

    @Override
    protected Class<CourseCategoryServiceModel> getModelClass() {
        return CourseCategoryServiceModel.class;
    }

    @Override
    protected Class<CourseCategory> getEntityClass() {
        return CourseCategory.class;
    }
}