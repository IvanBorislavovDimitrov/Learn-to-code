package com.code.to.learn.web.api_impl;

import com.code.to.learn.api.api.course_category.CourseCategoryServiceApi;
import com.code.to.learn.api.model.course_category.CourseCategoryBindingModel;
import com.code.to.learn.api.model.course_category.CourseCategoryResponseModel;
import com.code.to.learn.persistence.domain.model.CourseCategoryServiceModel;
import com.code.to.learn.persistence.service.api.CourseCategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("courseCategoryServiceApiImpl")
public class CourseCategoryServiceApiImpl implements CourseCategoryServiceApi {

    private final CourseCategoryService courseCategoryService;
    private final ModelMapper modelMapper;

    @Autowired
    public CourseCategoryServiceApiImpl(CourseCategoryService courseCategoryService, ModelMapper modelMapper) {
        this.courseCategoryService = courseCategoryService;
        this.modelMapper = modelMapper;
    }

    @Override
    public ResponseEntity<CourseCategoryResponseModel> add(CourseCategoryBindingModel courseCategoryBindingModel) {
        CourseCategoryServiceModel courseCategoryServiceModel = modelMapper.map(courseCategoryBindingModel, CourseCategoryServiceModel.class);
        courseCategoryService.save(courseCategoryServiceModel);
        CourseCategoryResponseModel courseCategoryResponseModel = modelMapper.map(courseCategoryServiceModel, CourseCategoryResponseModel.class);
        return ResponseEntity.ok(courseCategoryResponseModel);
    }
}
