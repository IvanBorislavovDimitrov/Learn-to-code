package com.code.to.learn.persistence.service.impl;

import com.code.to.learn.persistence.dao.api.CourseCategoryDao;
import com.code.to.learn.persistence.domain.entity.CourseCategory;
import com.code.to.learn.persistence.domain.model.CourseCategoryServiceModel;
import com.code.to.learn.persistence.service.api.CourseCategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

// TODO: Refactor -> Classes that have a name should extend a common predecessor
@Service
public class CourseCategoryServiceImpl extends GenericServiceImpl<CourseCategory, CourseCategoryServiceModel> implements CourseCategoryService {

    private final CourseCategoryDao courseCategoryDao;

    @Autowired
    protected CourseCategoryServiceImpl(CourseCategoryDao courseCategoryDao, ModelMapper modelMapper) {
        super(courseCategoryDao, modelMapper);
        this.courseCategoryDao = courseCategoryDao;
    }

    @Override
    public Optional<CourseCategoryServiceModel> findByName(String courseCategoryName) {
        Optional<CourseCategory> optionalCourseCategory = courseCategoryDao.findByName(courseCategoryName);
        return optionalCourseCategory.map(this::toOutput);
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