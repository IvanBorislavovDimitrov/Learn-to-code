package com.code.to.learn.web.api_impl;

import com.code.to.learn.api.api.course.CourseServiceApi;
import com.code.to.learn.api.model.course.CourseBindingModel;
import com.code.to.learn.api.model.course.CourseResponseModel;
import com.code.to.learn.core.validator.CourseValidator;
import com.code.to.learn.persistence.constant.Messages;
import com.code.to.learn.persistence.domain.model.CourseCategoryServiceModel;
import com.code.to.learn.persistence.domain.model.CourseServiceModel;
import com.code.to.learn.persistence.domain.model.UserServiceModel;
import com.code.to.learn.persistence.exception.basic.NotFoundException;
import com.code.to.learn.persistence.service.api.CourseCategoryService;
import com.code.to.learn.persistence.service.api.CourseService;
import com.code.to.learn.persistence.service.api.UserService;
import com.code.to.learn.util.mapper.ExtendableMapper;
import com.code.to.learn.web.util.MultipartFileUploader;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static com.code.to.learn.api.constant.Constants.DATE_PATTERN;
import static com.code.to.learn.web.constants.Constants.COURSE_VIDEO_EXTENSION;

@Component
public class CourseServiceApiImpl extends ExtendableMapper<CourseServiceModel, CourseResponseModel> implements CourseServiceApi {

    private final CourseService courseService;
    private final MultipartFileUploader multipartFileUploader;
    private final CourseValidator courseValidator;
    private final UserService userService;
    private final CourseCategoryService courseCategoryService;

    @Autowired
    public CourseServiceApiImpl(CourseService courseService, ModelMapper modelMapper, MultipartFileUploader multipartFileUploader, CourseValidator courseValidator, UserService userService, CourseCategoryService courseCategoryService) {
        super(modelMapper);
        this.courseService = courseService;
        this.multipartFileUploader = multipartFileUploader;
        this.courseValidator = courseValidator;
        this.userService = userService;
        this.courseCategoryService = courseCategoryService;
    }

    @Override
    public ResponseEntity<CourseResponseModel> addCourse(CourseBindingModel courseBindingModel) {
        courseValidator.validateCourseBindingModel(courseBindingModel);
        CourseServiceModel courseServiceModel = toCourseServiceModel(courseBindingModel);
        multipartFileUploader.uploadFile(courseBindingModel.getVideo(), courseServiceModel.getVideoName());
        courseService.save(courseServiceModel);
        return ResponseEntity.ok(toOutput(courseServiceModel));
    }

    private CourseServiceModel toCourseServiceModel(CourseBindingModel courseBindingModel) {
        CourseServiceModel courseServiceModel = getMapper().map(courseBindingModel, CourseServiceModel.class);
        String extension = FilenameUtils.getExtension(courseBindingModel.getVideo().getOriginalFilename());
        courseServiceModel.setVideoName(courseBindingModel.getName() + COURSE_VIDEO_EXTENSION + "." + extension);
        Optional<UserServiceModel> teacher = userService.findByUsername(courseBindingModel.getTeacherName());
        if (!teacher.isPresent()) {
            throw new NotFoundException(Messages.USER_NOT_FOUND, courseBindingModel.getTeacherName());
        }
        courseServiceModel.setTeacher(teacher.get());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
        courseServiceModel.setStartDate(LocalDate.parse(courseBindingModel.getStartDate(), formatter));
        courseServiceModel.setEndDate(LocalDate.parse(courseBindingModel.getEndDate(), formatter));
        Optional<CourseCategoryServiceModel> courseCategoryServiceModel = courseCategoryService.findByName(courseBindingModel.getCategoryName());
        if (!courseCategoryServiceModel.isPresent()) {
            throw new NotFoundException(Messages.CATEGORY_NOT_FOUND, courseBindingModel.getCategoryName());
        }
        courseServiceModel.setCourseCategory(courseCategoryServiceModel.get());
        return courseServiceModel;
    }

    @Override
    protected Class<CourseServiceModel> getInputClass() {
        return CourseServiceModel.class;
    }

    @Override
    protected Class<CourseResponseModel> getOutputClass() {
        return CourseResponseModel.class;
    }
}
