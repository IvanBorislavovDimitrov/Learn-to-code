package com.code.to.learn.web.api_impl;

import com.code.to.learn.api.api.course_category.CourseCategoryServiceApi;
import com.code.to.learn.api.model.course_category.CourseCategoryBindingModel;
import com.code.to.learn.api.model.course_category.CourseCategoryResponseModel;
import com.code.to.learn.persistence.domain.model.CourseCategoryServiceModel;
import com.code.to.learn.persistence.service.api.CourseCategoryService;
import com.code.to.learn.util.mapper.ExtendableMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CourseCategoryServiceApiImpl extends ExtendableMapper<CourseCategoryServiceModel, CourseCategoryResponseModel> implements CourseCategoryServiceApi {

    private final CourseCategoryService courseCategoryService;

    @Autowired
    public CourseCategoryServiceApiImpl(ModelMapper modelMapper, CourseCategoryService courseCategoryService) {
        super(modelMapper);
        this.courseCategoryService = courseCategoryService;
    }

    @Override
    public ResponseEntity<CourseCategoryResponseModel> add(CourseCategoryBindingModel courseCategoryBindingModel) {
        CourseCategoryServiceModel courseCategoryServiceModel = getMapper().map(courseCategoryBindingModel, CourseCategoryServiceModel.class);
        courseCategoryService.save(courseCategoryServiceModel);
        CourseCategoryResponseModel courseCategoryResponseModel = toOutput(courseCategoryServiceModel);
        return ResponseEntity.ok(courseCategoryResponseModel);
    }

    @Override
    protected Class<CourseCategoryServiceModel> getInputClass() {
        return CourseCategoryServiceModel.class;
    }

    @Override
    protected Class<CourseCategoryResponseModel> getOutputClass() {
        return CourseCategoryResponseModel.class;
    }
}
