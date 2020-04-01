package com.code.to.learn.web.api_impl;

import com.code.to.learn.api.api.course.CourseServiceApi;
import com.code.to.learn.api.model.course.CourseBindingModel;
import com.code.to.learn.api.model.course.CourseResponseModel;
import com.code.to.learn.core.validator.CourseValidator;
import com.code.to.learn.persistence.constant.Messages;
import com.code.to.learn.persistence.domain.model.CourseCategoryServiceModel;
import com.code.to.learn.persistence.domain.model.CourseServiceModel;
import com.code.to.learn.persistence.domain.model.UserServiceModel;
import com.code.to.learn.persistence.exception.basic.LCException;
import com.code.to.learn.persistence.exception.basic.NotFoundException;
import com.code.to.learn.persistence.service.api.CourseCategoryService;
import com.code.to.learn.persistence.service.api.CourseService;
import com.code.to.learn.persistence.service.api.UserService;
import com.code.to.learn.util.mapper.ExtendableMapper;
import com.code.to.learn.web.util.FileGetter;
import com.code.to.learn.web.util.FileToUpload;
import com.code.to.learn.web.util.MultipartFileUploader;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static com.code.to.learn.api.constant.Constants.DATE_PATTERN;
import static com.code.to.learn.web.constants.Constants.COURSE_VIDEO_EXTENSION;
import static com.code.to.learn.web.constants.Constants.THUMBNAIL_FILE_EXTENSION;

@Component
public class CourseServiceApiImpl extends ExtendableMapper<CourseServiceModel, CourseResponseModel> implements CourseServiceApi {

    private final CourseService courseService;
    private final MultipartFileUploader multipartFileUploader;
    private final CourseValidator courseValidator;
    private final UserService userService;
    private final CourseCategoryService courseCategoryService;
    private final ExecutorService executorService;
    private final FileGetter fileGetter;

    @Autowired
    public CourseServiceApiImpl(CourseService courseService, ModelMapper modelMapper, MultipartFileUploader multipartFileUploader,
                                CourseValidator courseValidator, UserService userService,
                                CourseCategoryService courseCategoryService, ExecutorService executorService, FileGetter fileGetter) {
        super(modelMapper);
        this.courseService = courseService;
        this.multipartFileUploader = multipartFileUploader;
        this.courseValidator = courseValidator;
        this.userService = userService;
        this.courseCategoryService = courseCategoryService;
        this.executorService = executorService;
        this.fileGetter = fileGetter;
    }

    @Override
    public ResponseEntity<CourseResponseModel> addCourse(CourseBindingModel courseBindingModel) {
        courseValidator.validateCourseBindingModel(courseBindingModel);
        CourseServiceModel courseServiceModel = toCourseServiceModel(courseBindingModel);
        FileToUpload thumbnail = new FileToUpload(courseServiceModel.getThumbnailName(), courseBindingModel.getThumbnail());
        FileToUpload video = new FileToUpload(courseServiceModel.getVideoName(), courseBindingModel.getVideo());
        multipartFileUploader.uploadFilesAsync(Arrays.asList(thumbnail, video));
        courseService.save(courseServiceModel);
        return ResponseEntity.ok(toOutput(courseServiceModel));
    }

    private CourseServiceModel toCourseServiceModel(CourseBindingModel courseBindingModel) {
        CourseServiceModel courseServiceModel = getMapper().map(courseBindingModel, CourseServiceModel.class);
        String videoFileExtension = FilenameUtils.getExtension(courseBindingModel.getVideo().getOriginalFilename());
        courseServiceModel.setVideoName(courseBindingModel.getName() + COURSE_VIDEO_EXTENSION + "." + videoFileExtension);
        String thumbnailFileExtension = FilenameUtils.getExtension(courseBindingModel.getThumbnail().getOriginalFilename());
        courseServiceModel.setThumbnailName(courseBindingModel.getName() + THUMBNAIL_FILE_EXTENSION + "." + thumbnailFileExtension);
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
    public ResponseEntity<List<CourseResponseModel>> getLatestCourses(int count) {
        List<CourseServiceModel> courseServiceModels = courseService.findLatestCourses(count);
        List<Future<CourseResponseModel>> courseResponseModelTasks = new ArrayList<>();
        for (CourseServiceModel courseServiceModel : courseServiceModels) {
            Future<CourseResponseModel> courseResponseModelTask = executorService.submit(() -> toCourseResponseModel(courseServiceModel));
            courseResponseModelTasks.add(courseResponseModelTask);
        }
        return ResponseEntity.ok(waitCourseResponseModelTasksToBeExecuted(courseResponseModelTasks));
    }

    private CourseResponseModel toCourseResponseModel(CourseServiceModel courseServiceModel) {
        CourseResponseModel courseResponseModel = toOutput(courseServiceModel);
        byte[] thumbnailContent = fileGetter.getImageResource(courseServiceModel.getThumbnailName());
        String base64ThumbnailContent = Base64.getEncoder().encodeToString(thumbnailContent);
        courseResponseModel.setBase64Thumbnail(base64ThumbnailContent);
        return courseResponseModel;
    }

    private List<CourseResponseModel> waitCourseResponseModelTasksToBeExecuted(List<Future<CourseResponseModel>> courseResponseModelTasks) {
        return courseResponseModelTasks.stream()
                .map(this::waitForCourseToBeFetched)
                .collect(Collectors.toList());
    }

    private CourseResponseModel waitForCourseToBeFetched(Future<CourseResponseModel> courseResponseModelFuture) {
        try {
            return courseResponseModelFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new LCException(e.getMessage(), e);
        }
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
