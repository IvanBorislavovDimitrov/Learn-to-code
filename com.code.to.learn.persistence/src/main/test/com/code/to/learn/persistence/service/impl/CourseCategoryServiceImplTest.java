package com.code.to.learn.persistence.service.impl;

import com.code.to.learn.persistence.dao.api.CourseCategoryDao;
import com.code.to.learn.persistence.domain.model.CourseCategoryWithCoursesNumber;
import com.code.to.learn.persistence.service.api.CourseCategoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.List;

public class CourseCategoryServiceImplTest {

    @Mock
    private ModelMapper modelMapper;
    @Mock
    private CourseCategoryDao courseCategoryDao;
    private CourseCategoryService courseCategoryService;

    public CourseCategoryServiceImplTest() {
        MockitoAnnotations.initMocks(this);
    }

    @BeforeEach
    public void setUp() {
        courseCategoryService = new CourseCategoryServiceImpl(courseCategoryDao, modelMapper);
    }

    @Test
    public void testFindCategoriesWithMostCourses() {
        List<CourseCategoryWithCoursesNumber> categoriesWithMostCourses = courseCategoryService.findCategoriesWithMostCourses();
        Assertions.assertEquals(0, categoriesWithMostCourses.size());
    }

}
