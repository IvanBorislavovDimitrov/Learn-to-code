package com.code.to.learn.persistence.service.api;

import com.code.to.learn.persistence.domain.model.CourseCategoryServiceModel;

import java.util.Optional;

public interface CourseCategoryService extends GenericService<CourseCategoryServiceModel> {

    Optional<CourseCategoryServiceModel> findByName(String courseCategoryName);
}
