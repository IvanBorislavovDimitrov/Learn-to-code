package com.code.to.learn.api.api.course;

import com.code.to.learn.api.model.course.CourseBindingModel;
import com.code.to.learn.api.model.course.CoursePagesResponseModel;
import com.code.to.learn.api.model.course.CourseResponseModel;
import com.code.to.learn.api.model.course.UserEnrolledForCourse;
import com.code.to.learn.api.util.UsernameGetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/courses")
public class CourseRestController {

    private final CourseServiceApi courseServiceApi;
    private final UsernameGetter usernameGetter;

    @Autowired
    public CourseRestController(CourseServiceApi courseServiceApi, UsernameGetter usernameGetter) {
        this.courseServiceApi = courseServiceApi;
        this.usernameGetter = usernameGetter;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CourseResponseModel> addCourse(@Valid CourseBindingModel courseBindingModel) {
        return courseServiceApi.add(courseBindingModel);
    }

    @GetMapping(value = "/latest", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CourseResponseModel>> getLatestCourses(@RequestParam int count,
                                                                      @RequestParam(defaultValue = "false") boolean loadThumbnails) {
        return courseServiceApi.getLatest(count, loadThumbnails);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CourseResponseModel>> getCourses(@RequestParam(defaultValue = "1") int page,
                                                                @RequestParam(required = false) String name,
                                                                @RequestParam(required = false) String category) {
        return courseServiceApi.getByPageNameCategory(page - 1, name, category);
    }

    @GetMapping(value = "/pages-count", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CoursePagesResponseModel> getPagesCount(@RequestParam(required = false) String courseName) {
        return courseServiceApi.getPagesCount(courseName);
    }

    @GetMapping(value = "/{courseName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CourseResponseModel> getCourse(@PathVariable String courseName) {
        return courseServiceApi.get(courseName);
    }

    @PostMapping(value = "/enroll/{courseName}", produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<CourseResponseModel> enrollUserForCourse(@PathVariable String courseName) {
        String loggedUser = usernameGetter.getLoggedInUserUsername();
        return courseServiceApi.enrollUserForCourse(loggedUser, courseName);
    }

    @GetMapping(value = "/is-enrolled/{courseEnrolledFor}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserEnrolledForCourse> isLoggedUserEnrolledForCourse(@PathVariable String courseEnrolledFor) {
        String loggedUser = usernameGetter.getLoggedInUserUsername();
        return courseServiceApi.isUserEnrolledForCourse(loggedUser, courseEnrolledFor);
    }

    @PostMapping(value = "/cart/add/{courseName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CourseResponseModel> addToCart(@PathVariable String courseName) {
        String loggedUser = usernameGetter.getLoggedInUserUsername();
        return courseServiceApi.addToCart(loggedUser, courseName);
    }

    @GetMapping(value = "/cart/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CourseResponseModel>> getCoursesInCart() {
        String loggedUser = usernameGetter.getLoggedInUserUsername();
        return courseServiceApi.getCoursesInCart(loggedUser);
    }

    @PostMapping(value = "/cart/remove/{courseName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CourseResponseModel> removeItemFromCart(@PathVariable String courseName) {
        String loggedUser = usernameGetter.getLoggedInUserUsername();
        return courseServiceApi.removeCourseFromCart(loggedUser, courseName);
    }

    @PostMapping(value = "/cart/enroll-from-cart")
    public ResponseEntity<List<CourseResponseModel>> enrollForCourses() {
        String loggedUser = usernameGetter.getLoggedInUserUsername();
        return courseServiceApi.enrollFromCart(loggedUser);
    }

}
