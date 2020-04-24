package com.code.to.learn.persistence.service.api;

import com.code.to.learn.persistence.domain.entity.CourseCategory;
import com.code.to.learn.persistence.domain.model.CourseCategoryServiceModel;
import com.code.to.learn.persistence.domain.model.CourseCategoryWithCoursesNumber;

import java.util.List;

public interface CourseCategoryService extends GenericService<CourseCategoryServiceModel>, NamedElementService<CourseCategory, CourseCategoryServiceModel> {

    List<CourseCategoryWithCoursesNumber> findCategoriesWithMostCourses();

}
