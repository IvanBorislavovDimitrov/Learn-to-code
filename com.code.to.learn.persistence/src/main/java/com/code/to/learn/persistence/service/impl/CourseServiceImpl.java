package com.code.to.learn.persistence.service.impl;

import com.code.to.learn.persistence.domain.entity.Course;
import com.code.to.learn.persistence.domain.model.CourseServiceModel;
import com.code.to.learn.persistence.repository.api.CourseRepository;
import com.code.to.learn.persistence.service.api.CourseService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseServiceImpl extends GenericServiceImpl<Course, CourseServiceModel> implements CourseService {

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository, ModelMapper modelMapper) {
        super(courseRepository, modelMapper);
    }

    @Override
    protected Class<CourseServiceModel> getModelClass() {
        return CourseServiceModel.class;
    }

    @Override
    protected Class<Course> getEntityClass() {
        return Course.class;
    }
}
