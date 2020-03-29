package com.code.to.learn.persistence.service.api;

import com.code.to.learn.persistence.domain.model.CourseServiceModel;

public interface CourseService extends GenericService<CourseServiceModel> {

    boolean isNameTaken(String courseName);
}
