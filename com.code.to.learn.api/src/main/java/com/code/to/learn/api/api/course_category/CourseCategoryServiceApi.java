package com.code.to.learn.api.api.course_category;

import com.code.to.learn.api.model.course_category.CourseCategoryBindingModel;
import com.code.to.learn.api.model.course_category.CourseCategoryResponseModel;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CourseCategoryServiceApi {

    ResponseEntity<CourseCategoryResponseModel> add(CourseCategoryBindingModel courseCategoryBindingModel);

    ResponseEntity<CourseCategoryResponseModel> update(CourseCategoryBindingModel courseCategoryBindingModel);

    ResponseEntity<List<CourseCategoryResponseModel>> getCourseCategories();

    ResponseEntity<List<CourseCategoryResponseModel>> getCategoriesWithMostCourses(int limit);
}
