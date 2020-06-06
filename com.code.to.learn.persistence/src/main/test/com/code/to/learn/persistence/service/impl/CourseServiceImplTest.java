package com.code.to.learn.persistence.service.impl;

import com.code.to.learn.persistence.dao.api.CourseDao;
import com.code.to.learn.persistence.dao.api.UserDao;
import com.code.to.learn.persistence.domain.entity.Course;
import com.code.to.learn.persistence.domain.entity.User;
import com.code.to.learn.persistence.domain.model.CourseServiceModel;
import com.code.to.learn.persistence.service.api.CourseService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Matchers.anyString;

public class CourseServiceImplTest {

    @Mock
    private ModelMapper modelMapper;
    @Mock
    private CourseDao courseDao;
    @Mock
    private UserDao userDao;
    private CourseService courseService;

    public CourseServiceImplTest() {
        MockitoAnnotations.initMocks(this);
    }

    @BeforeEach
    public void setUp() {
        courseService = new CourseServiceImpl(courseDao, modelMapper, userDao);
    }

    @Test
    public void testIsNameTaken() {
        Mockito.when(courseDao.findByName(anyString())).thenReturn(Optional.empty());
        Assertions.assertFalse(courseService.isNameTaken("123"));
    }

    @Test
    public void testFindCourses() {
        List<CourseServiceModel> courses = courseService.findCourses(1, 1, "123", "java");
        Assertions.assertEquals(0, courses.size());
    }

    @Test
    public void testCountByNameLike() {
        Assertions.assertEquals(0, courseService.countByNameLike("123"));
    }

    @Test
    public void testEnrollUserForCourse() {
        User user = Mockito.mock(User.class);
        Mockito.when(user.getCourses()).thenReturn(new ArrayList<>());
        Mockito.when(userDao.findByUsername(anyString())).thenReturn(Optional.of(user));
        Course course = Mockito.mock(Course.class);
        Mockito.when(course.getAttendants()).thenReturn(new ArrayList<>());
        Mockito.when(courseDao.findByName(anyString())).thenReturn(Optional.of(course));
        Mockito.when(courseDao.update(course)).thenReturn(Optional.of(course));
        courseService.enrollUserForCourse("123", "123");
        Mockito.verify(userDao).update(user);
    }

    @Test
    public void testGetCoursesFromCart() {
        User user = Mockito.mock(User.class);
        Mockito.when(user.getCoursesInCart()).thenReturn(Collections.emptyList());
        Mockito.when(userDao.findByUsername(anyString())).thenReturn(Optional.of(user));
        Assertions.assertEquals(0, courseService.getCoursesInCart("123").size());
    }

    @Test
    public void testRemoveCourseFromCart() {
        User user = Mockito.mock(User.class);
        Mockito.when(user.getCoursesInCart()).thenReturn(Collections.emptyList());
        Mockito.when(userDao.findByUsername(anyString())).thenReturn(Optional.of(user));
        Course course = Mockito.mock(Course.class);
        Mockito.when(courseDao.findByName(anyString())).thenReturn(Optional.of(course));
        Mockito.when(courseDao.update(course)).thenReturn(Optional.of(course));
        courseService.removeCourseFromCart("123", "123");
        Mockito.verify(userDao).update(user);
    }

    @Test
    public void testEmptyCart() {
        User user = Mockito.mock(User.class);
        Mockito.when(user.getCoursesInCart()).thenReturn(Collections.emptyList());
        Mockito.when(userDao.findByUsername(anyString())).thenReturn(Optional.of(user));
        courseService.emptyCart("use");
        Mockito.verify(userDao).update(user);
    }

    @Test
    public void testFindMostCommented() {
        Assertions.assertEquals(0, courseService.findMostCommented(1).size());
    }

    @Test
    public void testFindBestSellers() {
        Assertions.assertEquals(0, courseService.findBestSellers(1).size());
    }

    @Test
    public void testMarkCourseAsRatedByUser() {
        Course course = Mockito.mock(Course.class);
        Mockito.when(courseDao.markCourseAsRatedByUser(anyString(), anyString())).thenReturn(course);
        CourseServiceModel mockedCourseServiceModel = Mockito.mock(CourseServiceModel.class);
        Mockito.when(mockedCourseServiceModel.getName()).thenReturn("asd");
        Mockito.when(modelMapper.map(course, CourseServiceModel.class)).thenReturn(mockedCourseServiceModel);
        CourseServiceModel courseServiceModel = courseService.markCourseAsRatedByUser("123", "123");
        Assertions.assertEquals("asd", courseServiceModel.getName());
    }

    @Test
    public void testHasUserRatedCourses() {
        Assertions.assertFalse(courseService.hasUserRatedCourse("123", "123"));
    }

}
