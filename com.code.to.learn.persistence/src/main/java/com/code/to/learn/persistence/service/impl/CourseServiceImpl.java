package com.code.to.learn.persistence.service.impl;

import com.code.to.learn.persistence.dao.api.CourseDao;
import com.code.to.learn.persistence.domain.entity.Course;
import com.code.to.learn.persistence.domain.model.CourseServiceModel;
import com.code.to.learn.persistence.service.api.CourseService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl extends NamedElementServiceImpl<Course, CourseServiceModel> implements CourseService {

    private final CourseDao courseDao;

    @Autowired
    public CourseServiceImpl(CourseDao courseDao, ModelMapper modelMapper) {
        super(courseDao, modelMapper);
        this.courseDao = courseDao;
    }

    @Override
    public boolean isNameTaken(String courseName) {
        return courseDao.findByName(courseName).isPresent();
    }

    @Override
    public List<CourseServiceModel> findLatestCourses(int count) {
        return toOutput(courseDao.findLatestCourses(count));
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
