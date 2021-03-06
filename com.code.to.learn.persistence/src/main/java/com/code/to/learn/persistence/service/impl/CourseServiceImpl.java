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
import java.util.Objects;
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
        User user = getOrThrowNotFound(() -> userDao.findByUsername(username), Messages.USERNAME_NOT_FOUND, username);
        Course course = getOrThrowNotFound(() -> courseDao.findByName(courseName), Messages.COURSE_NOT_FOUND, courseName);
        user.getCourses().add(course);
        user.getUnpaidCourses().add(course);
        userDao.update(user);
        course.getAttendants().add(user);
        course.getUsersWhoHaveNotPaid().add(user);
        Course updatedCourse = getWithoutCheck(() -> courseDao.update(course));
        removeFromCart(user, courseName);
        return toOutput(updatedCourse);
    }

    @Override
    public CourseServiceModel addToCart(String username, String courseName) {
        User user = getOrThrowNotFound(() -> userDao.findByUsername(username), Messages.USERNAME_NOT_FOUND, username);
        Course course = getOrThrowNotFound(() -> courseDao.findByName(courseName), Messages.COURSE_NOT_FOUND, courseName);
        user.getCoursesInCart().add(course);
        userDao.update(user);
        course.getFutureAttendants().add(user);
        Course updatedCourse = getWithoutCheck(() -> courseDao.update(course));
        return toOutput(updatedCourse);
    }

    @Override
    public List<CourseServiceModel> getCoursesInCart(String username) {
        User user = getOrThrowNotFound(() -> userDao.findByUsername(username), Messages.USERNAME_NOT_FOUND, username);
        return toOutput(user.getCoursesInCart());
    }

    @Override
    public CourseServiceModel removeCourseFromCart(String username, String courseName) {
        User user = getOrThrowNotFound(() -> userDao.findByUsername(username), Messages.USERNAME_NOT_FOUND, username);
        user.getCoursesInCart().removeIf(course -> Objects.equals(course.getName(), courseName));
        userDao.update(user);
        Course course = getOrThrowNotFound(() -> courseDao.findByName(courseName), Messages.COURSE_NOT_FOUND, courseName);
        course.getFutureAttendants().removeIf(attendant -> Objects.equals(attendant.getUsername(), user.getUsername()));
        Course updatedCourse = getWithoutCheck(() -> courseDao.update(course));
        return toOutput(updatedCourse);
    }

    @Override
    public void emptyCart(String username) {
        User user = getOrThrowNotFound(() -> userDao.findByUsername(username), Messages.USERNAME_NOT_FOUND, username);
        List<Course> coursesInCart = user.getCoursesInCart();
        for (Course course : coursesInCart) {
            course.getFutureAttendants().removeIf(attendant -> Objects.equals(attendant.getUsername(), user.getUsername()));
            courseDao.update(course);
        }
        user.emptyCart();
        userDao.update(user);
    }

    private void removeFromCart(User user, String courseName) {
        Optional<Course> course = getCourseInCartByNameForUser(user, courseName);
        if (!course.isPresent()) {
            return;
        }
        user.getCoursesInCart().remove(course.get());
        course.get().getFutureAttendants().remove(user);
        userDao.update(user);
        courseDao.update(course.get());
    }

    private Optional<Course> getCourseInCartByNameForUser(User user, String courseName) {
        return user.getCoursesInCart()
                .stream()
                .filter(course -> Objects.equals(course.getName(), courseName))
                .findFirst();
    }

    @Override
    public List<CourseServiceModel> findBestSellers(int limit) {
        List<Course> bestSellers = courseDao.findBestSellers(limit);
        return toOutput(bestSellers);
    }

    @Override
    public List<CourseServiceModel> findMostCommented(int limit) {
        List<Course> mostCommented = courseDao.findMostCommented(limit);
        return toOutput(mostCommented);
    }

    @Override
    public CourseServiceModel markCourseAsRatedByUser(String courseName, String username) {
        Course course = courseDao.markCourseAsRatedByUser(courseName, username);
        return toOutput(course);
    }

    @Override
    public boolean hasUserRatedCourse(String courseName, String username) {
        return courseDao.hasUserRatedCourse(courseName, username);
    }

    @Override
    public long getVideoSize(String courseName, String videoName) {
        return courseDao.getVideoSize(courseName, videoName);
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
