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
@RequestMapping(value = "/course-categories", produces = MediaType.APPLICATION_JSON_VALUE)
public class CourseCategoryRestController {

    private final CourseCategoryServiceApi courseCategoryServiceApi;

    @Autowired
    public CourseCategoryRestController(CourseCategoryServiceApi courseCategoryServiceApi) {
        this.courseCategoryServiceApi = courseCategoryServiceApi;
    }

    @PostMapping(value ="/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CourseCategoryResponseModel> add(@Valid CourseCategoryBindingModel courseCategoryBindingModel) {
        if (courseCategoryBindingModel.getThumbnail().getSize() > 20971520) {
            throw new IllegalArgumentException("File too big");
        }
        return courseCategoryServiceApi.add(courseCategoryBindingModel);
    }

    @PostMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CourseCategoryResponseModel> update(@Valid CourseCategoryBindingModel courseCategoryBindingModel) {
        if (courseCategoryBindingModel.getThumbnail().getSize() > 20971520) {
            throw new IllegalArgumentException("File too big");
        }
        return courseCategoryServiceApi.update(courseCategoryBindingModel);
    }

    @GetMapping
    public ResponseEntity<List<CourseCategoryResponseModel>> getCourseCategories() {
        return courseCategoryServiceApi.getCourseCategories();
    }

    @GetMapping(value = "/top")
    public ResponseEntity<List<CourseCategoryResponseModel>> getCategoriesWithMostCourses(@RequestParam(defaultValue = "6") int limit) {
        return courseCategoryServiceApi.getCategoriesWithMostCourses(limit);
    }

    @GetMapping(value = "/{courseCategoryName}")
    public ResponseEntity<CourseCategoryResponseModel> getCourseCategory(@PathVariable String courseCategoryName) {
        return courseCategoryServiceApi.getByName(courseCategoryName);
    }
}
