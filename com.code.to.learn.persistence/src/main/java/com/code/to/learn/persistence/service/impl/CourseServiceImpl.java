package com.code.to.learn.persistence.service.impl;

import com.code.to.learn.persistence.constant.Messages;
import com.code.to.learn.persistence.dao.api.CourseDao;
import com.code.to.learn.persistence.dao.api.UserDao;
import com.code.to.learn.persistence.domain.entity.Course;
import com.code.to.learn.persistence.domain.entity.User;
import com.code.to.learn.persistence.domain.model.CourseServiceModel;
import com.code.to.learn.persistence.service.api.CourseService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl extends NamedElementServiceImpl<Course, CourseServiceModel> implements CourseService {

    private final CourseDao courseDao;
    private final UserDao userDao;

    @Autowired
    public CourseServiceImpl(CourseDao courseDao, ModelMapper modelMapper, UserDao userDao) {
        super(courseDao, modelMapper);
        this.courseDao = courseDao;
        this.userDao = userDao;
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
    public List<CourseServiceModel> findCourses(int page, int maxResults, String name, String category) {
        return toOutput(courseDao.findCourses(page, maxResults, name, category));
    }

    @Override
    public long countByNameLike(String name) {
        return courseDao.countByNameLike(name);
    }

    @Override
    public CourseServiceModel enrollUserForCourse(String username, String courseName) {
        User user = getOrThrow(() -> userDao.findByUsername(username), Messages.USERNAME_NOT_FOUND, username);
        Course course = getOrThrow(() -> courseDao.findByName(courseName), Messages.COURSE_NOT_FOUND, courseName);
        user.getCourses().add(course);
        userDao.update(user);
        course.getAttendants().add(user);
        Course updatedCourse = getOrThrow(() -> courseDao.update(course));
        return toOutput(updatedCourse);
    }

    @Override
    public CourseServiceModel addToCart(String username, String courseName) {
        User user = getOrThrow(() -> userDao.findByUsername(username), Messages.USERNAME_NOT_FOUND, username);
        Course course = getOrThrow(() -> courseDao.findByName(courseName), Messages.COURSE_NOT_FOUND, courseName);
        user.getCoursesInCart().add(course);
        userDao.update(user);
        course.getFutureAttendants().add(user);
        Course updatedCourse = getOrThrow(() -> courseDao.update(course));
        return toOutput(updatedCourse);
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
