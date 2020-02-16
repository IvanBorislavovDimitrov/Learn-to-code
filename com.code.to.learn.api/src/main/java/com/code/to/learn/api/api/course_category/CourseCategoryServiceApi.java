package com.code.to.learn.api.api.course_category;

import com.code.to.learn.api.model.course_category.CourseCategoryBindingModel;
import com.code.to.learn.api.model.course_category.CourseCategoryResponseModel;
import org.springframework.http.ResponseEntity;

public interface CourseCategoryServiceApi {

    ResponseEntity<CourseCategoryResponseModel> add(CourseCategoryBindingModel courseCategoryBindingModel);
}
