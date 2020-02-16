package com.code.to.learn.api.api.course_category;

import com.code.to.learn.api.model.course_category.CourseCategoryBindingModel;
import com.code.to.learn.api.model.course_category.CourseCategoryResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/course-categories")
public class CourseCategoryRestController {

    private final CourseCategoryServiceApi courseCategoryServiceApi;

    @Autowired
    public CourseCategoryRestController(CourseCategoryServiceApi courseCategoryServiceApi) {
        this.courseCategoryServiceApi = courseCategoryServiceApi;
    }

    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CourseCategoryResponseModel> add(@RequestBody @Valid CourseCategoryBindingModel courseCategoryBindingModel) {
        return courseCategoryServiceApi.add(courseCategoryBindingModel);
    }
}
