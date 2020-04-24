package com.code.to.learn.api.api.course_category;

import com.code.to.learn.api.model.course_category.CourseCategoryBindingModel;
import com.code.to.learn.api.model.course_category.CourseCategoryResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/course-categories")
public class CourseCategoryRestController {

    private final CourseCategoryServiceApi courseCategoryServiceApi;

    @Autowired
    public CourseCategoryRestController(CourseCategoryServiceApi courseCategoryServiceApi) {
        this.courseCategoryServiceApi = courseCategoryServiceApi;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CourseCategoryResponseModel> add(@RequestBody @Valid CourseCategoryBindingModel courseCategoryBindingModel) {
        return courseCategoryServiceApi.add(courseCategoryBindingModel);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CourseCategoryResponseModel>> getCourseCategories() {
        return courseCategoryServiceApi.getCourseCategories();
    }

    @GetMapping(value = "/top", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CourseCategoryResponseModel>> getCategoriesWithMostCourses(@RequestParam(defaultValue = "6") int limit) {
        return courseCategoryServiceApi.getCategoriesWithMostCourses(limit);
    }
}
