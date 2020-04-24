package com.code.to.learn.persistence.service.impl;

import com.code.to.learn.persistence.dao.api.CourseCategoryDao;
import com.code.to.learn.persistence.domain.entity.CourseCategory;
import com.code.to.learn.persistence.domain.model.CourseCategoryServiceModel;
import com.code.to.learn.persistence.domain.model.CourseCategoryWithCoursesNumber;
import com.code.to.learn.persistence.service.api.CourseCategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseCategoryServiceImpl extends NamedElementServiceImpl<CourseCategory, CourseCategoryServiceModel> implements CourseCategoryService {


    @Autowired
    protected CourseCategoryServiceImpl(CourseCategoryDao courseCategoryDao, ModelMapper modelMapper) {
        super(courseCategoryDao, modelMapper);
    }

    @Override
    public List<CourseCategoryWithCoursesNumber> findCategoriesWithMostCourses() {
        List<CourseCategory> courseCategories = getGenericDao().findAll();
        return toCourseCategoryWithServiceNumber(courseCategories);
    }

    private List<CourseCategoryWithCoursesNumber> toCourseCategoryWithServiceNumber(List<CourseCategory> courseCategories) {
        return courseCategories.stream()
                .map(course -> new CourseCategoryWithCoursesNumber(course.getName(), course.getDescription(), course.getCourses().size()))
                .collect(Collectors.toList());
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