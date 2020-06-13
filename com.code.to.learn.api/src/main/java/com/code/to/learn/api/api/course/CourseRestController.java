package com.code.to.learn.api.api.course;

import com.code.to.learn.api.model.course.*;
import com.code.to.learn.api.util.UsernameGetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/courses", produces = MediaType.APPLICATION_JSON_VALUE)
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

    @GetMapping(value = "/latest")
    public ResponseEntity<List<CourseResponseModel>> getLatestCourses(@RequestParam int count,
                                                                      @RequestParam(defaultValue = "false") boolean loadThumbnails) {
        return courseServiceApi.getLatest(count, loadThumbnails);
    }

    @GetMapping
    public ResponseEntity<List<CourseResponseModel>> getCourses(@RequestParam(defaultValue = "1") int page,
                                                                @RequestParam(required = false) String name,
                                                                @RequestParam(required = false) String category) {
        return courseServiceApi.getByPageNameCategory(page - 1, name, category);
    }

    @GetMapping(value = "/pages-count")
    public ResponseEntity<CoursePagesResponseModel> getPagesCount(@RequestParam(required = false) String courseName) {
        return courseServiceApi.getPagesCount(courseName);
    }

    @GetMapping(value = "/{courseName}")
    public ResponseEntity<CourseResponseModel> getCourse(@PathVariable String courseName) {
        return courseServiceApi.get(courseName);
    }

    @PostMapping(value = "/enroll/{courseName}", consumes = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<CourseResponseModel> enrollUserForCourse(@PathVariable String courseName) {
        String loggedUser = usernameGetter.getLoggedInUserUsername();
        return courseServiceApi.enrollUserForCourse(loggedUser, courseName);
    }

    @GetMapping(value = "/is-enrolled/{courseEnrolledFor}")
    public ResponseEntity<UserEnrolledForCourse> isLoggedUserEnrolledForCourse(@PathVariable String courseEnrolledFor) {
        String loggedUser = usernameGetter.getLoggedInUserUsername();
        return courseServiceApi.isUserEnrolledForCourse(loggedUser, courseEnrolledFor);
    }

    @GetMapping(value = "/has-paid/{courseName}")
    public ResponseEntity<UserPaidForCourse> hasUserPaidForCourse(@PathVariable String courseName) {
        String loggedUser = usernameGetter.getLoggedInUserUsername();
        return courseServiceApi.hasUserPaidForCourse(loggedUser, courseName);
    }

    @PostMapping(value = "/has-paid/{courseName}/{username}")
    public ResponseEntity<CourseResponseModel> confirmUserHasPaidForCourse(@PathVariable String courseName, @PathVariable String username) {
        return courseServiceApi.confirmUserHasPaidForCourse(username, courseName);
    }

    @GetMapping(value = "/unpaid")
    public ResponseEntity<List<UnpaidCourseResponseModel>> getCoursesThatAreStillNotPaid() {
        return courseServiceApi.getCoursesThatAreStillUnpaid();
    }

    @PostMapping(value = "/cart/add/{courseName}")
    public ResponseEntity<CourseResponseModel> addToCart(@PathVariable String courseName) {
        String loggedUser = usernameGetter.getLoggedInUserUsername();
        return courseServiceApi.addToCart(loggedUser, courseName);
    }

    @GetMapping(value = "/cart/all")
    public ResponseEntity<List<CourseResponseModel>> getCoursesInCart() {
        String loggedUser = usernameGetter.getLoggedInUserUsername();
        return courseServiceApi.getCoursesInCart(loggedUser);
    }

    @PostMapping(value = "/cart/remove/{courseName}")
    public ResponseEntity<CourseResponseModel> removeItemFromCart(@PathVariable String courseName) {
        String loggedUser = usernameGetter.getLoggedInUserUsername();
        return courseServiceApi.removeCourseFromCart(loggedUser, courseName);
    }

    @PostMapping(value = "/cart/enroll-from-cart")
    public ResponseEntity<List<CourseResponseModel>> enrollForCourses() {
        String loggedUser = usernameGetter.getLoggedInUserUsername();
        return courseServiceApi.enrollFromCart(loggedUser);
    }

    @PostMapping(value = "/update")
        public ResponseEntity<CourseResponseModel> updateCourse(@Valid CourseBindingModel courseBindingModel,
                                                            @RequestParam(defaultValue = "false") boolean shouldUpdateContent) {
        return courseServiceApi.updateCourse(courseBindingModel, shouldUpdateContent);
    }

    @DeleteMapping(value = "/delete/{courseName}")
    public ResponseEntity<CourseResponseModel> deleteCourse(@PathVariable String courseName) {
        return courseServiceApi.deleteCourse(courseName);
    }

    @GetMapping(value = "/get")
    public ResponseEntity<List<CourseResponseModel>> getCourseByFilter(@RequestParam CourseFilter filter, @RequestParam int limit) {
        return courseServiceApi.getCoursesByFilter(filter, limit);
    }

    @GetMapping(value = "/user/{username}")
    public ResponseEntity<List<CourseResponseModel>> getCoursesByUser(@PathVariable String username) {
        return courseServiceApi.getCoursesByUsername(username);
    }

    @GetMapping(value = "/user/teaches/{username}")
    public ResponseEntity<List<CourseResponseModel>> getCoursesThatUserTeaches(@PathVariable String username) {
        return courseServiceApi.getCoursesThatUserTeaches(username);
    }

    @PostMapping(value = "/rate")
    public ResponseEntity<CourseResponseModel> rateCourse(@RequestBody @Valid CourseRatingBindingModel courseRatingBindingModel) {
        String loggedUserUsername = usernameGetter.getLoggedInUserUsername();
        return courseServiceApi.rateCourse(courseRatingBindingModel, loggedUserUsername);
    }

    @GetMapping(value = "/is-rated")
    public ResponseEntity<RatedCourseResponseModel> hasUserRatedCourse(@RequestParam String courseName) {
        String loggedUserUsername = usernameGetter.getLoggedInUserUsername();
        return courseServiceApi.hasUserRatedCourse(courseName, loggedUserUsername);
    }

}
