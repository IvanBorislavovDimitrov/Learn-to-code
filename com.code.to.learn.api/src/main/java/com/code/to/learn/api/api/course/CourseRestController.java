package com.code.to.learn.api.api.course;

import com.code.to.learn.api.model.course.CourseBindingModel;
import com.code.to.learn.api.model.course.CourseResponseModel;
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

    @Autowired
    public CourseRestController(CourseServiceApi courseServiceApi) {
        this.courseServiceApi = courseServiceApi;
    }

    @PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CourseResponseModel> addCourse(@Valid CourseBindingModel courseBindingModel) {
        return courseServiceApi.addCourse(courseBindingModel);
    }

    @GetMapping(value = "/latest", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CourseResponseModel>> getLatestCourses(@RequestParam int count, @RequestParam(defaultValue = "false") boolean loadThumbnails) {
        return courseServiceApi.getLatestCourses(count, loadThumbnails);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CourseResponseModel>> getCourses(@RequestParam(defaultValue = "1") int page) {
        return courseServiceApi.getCoursesByPage(page - 1);
    }



}
