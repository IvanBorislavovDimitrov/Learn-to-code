package com.code.to.learn.persistence.service.impl;

import com.code.to.learn.persistence.constant.Messages;
import com.code.to.learn.persistence.dao.api.CourseDao;
import com.code.to.learn.persistence.dao.api.UserDao;
import com.code.to.learn.persistence.domain.entity.Course;
import com.code.to.learn.persistence.domain.entity.User;
import com.code.to.learn.persistence.domain.model.CourseServiceModel;
import com.code.to.learn.persistence.exception.basic.NotFoundException;
import com.code.to.learn.persistence.service.api.CourseService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        Optional<User> user = userDao.findByUsername(username);
        if (!user.isPresent()) {
            throw new UsernameNotFoundException(username);
        }
        Optional<Course> course = courseDao.findByName(courseName);
        if (!course.isPresent()) {
            throw new NotFoundException(Messages.COURSE_NOT_FOUND, courseName);
        }
        user.get().getCourses().add(course.get());
        userDao.update(user.get());
        course.get().getAttendants().add(user.get());
        Optional<Course> updatedCourse = courseDao.update(course.get());
        return toOutput(updatedCourse.get());
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
