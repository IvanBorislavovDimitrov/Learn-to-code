package com.code.to.learn.web.api_impl;

import com.code.to.learn.api.api.course.CourseServiceApi;
import com.code.to.learn.api.model.course.CourseBindingModel;
import com.code.to.learn.api.model.course.CoursePagesResponseModel;
import com.code.to.learn.api.model.course.CourseResponseModel;
import com.code.to.learn.api.model.course.UserEnrolledForCourse;
import com.code.to.learn.core.environment.ApplicationConfiguration;
import com.code.to.learn.core.validator.CourseValidator;
import com.code.to.learn.persistence.domain.model.CourseCategoryServiceModel;
import com.code.to.learn.persistence.domain.model.CourseServiceModel;
import com.code.to.learn.persistence.domain.model.UserServiceModel;
import com.code.to.learn.persistence.exception.basic.LCException;
import com.code.to.learn.persistence.service.api.CourseCategoryService;
import com.code.to.learn.persistence.service.api.CourseService;
import com.code.to.learn.persistence.service.api.UserService;
import com.code.to.learn.util.mapper.ExtendableMapper;
import com.code.to.learn.web.util.FileToUpload;
import com.code.to.learn.web.util.RemoteStorageFileGetter;
import com.code.to.learn.web.util.RemoteStorageFileUploader;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static com.code.to.learn.api.constant.Constants.DATE_PATTERN;
import static com.code.to.learn.web.constants.Constants.*;

@Component
public class CourseServiceApiImpl extends ExtendableMapper<CourseServiceModel, CourseResponseModel> implements CourseServiceApi {

    private final CourseService courseService;
    private final RemoteStorageFileUploader remoteStorageFileUploader;
    private final CourseValidator courseValidator;
    private final UserService userService;
    private final CourseCategoryService courseCategoryService;
    private final ExecutorService executorService;
    private final RemoteStorageFileGetter remoteStorageFileGetter;
    private final ApplicationConfiguration configuration;

    @Autowired
    public CourseServiceApiImpl(CourseService courseService, ModelMapper modelMapper, RemoteStorageFileUploader remoteStorageFileUploader,
                                CourseValidator courseValidator, UserService userService,
                                CourseCategoryService courseCategoryService, ExecutorService executorService,
                                RemoteStorageFileGetter remoteStorageFileGetter, ApplicationConfiguration configuration) {
        super(modelMapper);
        this.courseService = courseService;
        this.remoteStorageFileUploader = remoteStorageFileUploader;
        this.courseValidator = courseValidator;
        this.userService = userService;
        this.courseCategoryService = courseCategoryService;
        this.executorService = executorService;
        this.remoteStorageFileGetter = remoteStorageFileGetter;
        this.configuration = configuration;
    }

    @Override
    public ResponseEntity<CourseResponseModel> add(CourseBindingModel courseBindingModel) {
        courseValidator.validateCourseBindingModel(courseBindingModel);
        CourseServiceModel courseServiceModel = toCourseServiceModel(courseBindingModel);
        List<FileToUpload> filesToUpload = getFilesToUpload(courseBindingModel);
        remoteStorageFileUploader.uploadFilesAsync(filesToUpload);
        courseService.save(courseServiceModel);
        return ResponseEntity.ok(toOutput(courseServiceModel));
    }

    private CourseServiceModel toCourseServiceModel(CourseBindingModel courseBindingModel) {
        CourseServiceModel courseServiceModel = getMapper().map(courseBindingModel, CourseServiceModel.class);
        courseServiceModel.setVideosNames(getVideosNames(courseBindingModel));
        courseServiceModel.setThumbnailName(getThumbnailName(courseBindingModel));
        UserServiceModel teacher = userService.findByUsername(courseBindingModel.getTeacherName());
        courseServiceModel.setTeacher(teacher);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
        courseServiceModel.setStartDate(LocalDate.parse(courseBindingModel.getStartDate(), formatter));
        courseServiceModel.setEndDate(LocalDate.parse(courseBindingModel.getEndDate(), formatter));
        CourseCategoryServiceModel courseCategoryServiceModel = courseCategoryService.findByName(courseBindingModel.getCategoryName());
        courseServiceModel.setCourseCategory(courseCategoryServiceModel);
        return courseServiceModel;
    }

    private List<FileToUpload> getFilesToUpload(CourseBindingModel courseBindingModel) {
        List<FileToUpload> filesToUploads = new ArrayList<>();
        courseBindingModel.getVideosToUpload().forEach((videoName, videoFile) -> {
            FileToUpload fileToUpload = new FileToUpload(getVideoFileName(courseBindingModel.getName(), videoName, videoFile), videoFile);
            filesToUploads.add(fileToUpload);
        });
        FileToUpload thumbnail = new FileToUpload(getThumbnailName(courseBindingModel), courseBindingModel.getThumbnail());
        filesToUploads.add(thumbnail);
        return filesToUploads;
    }

    private String getVideoFileName(String courseName, String videoName, MultipartFile videoFile) {
        String videoFileExtension = FilenameUtils.getExtension(videoFile.getOriginalFilename());
        return courseName + DELIMITER + videoName + COURSE_VIDEO_EXTENSION + "." + videoFileExtension;
    }

    private List<CourseServiceModel.CourseVideoServiceModel> getVideosNames(CourseBindingModel courseBindingModel) {
        return courseBindingModel.getVideosToUpload().entrySet()
                .stream()
                .map(videoToUpload -> new CourseServiceModel.CourseVideoServiceModel(videoToUpload.getKey(),
                        getVideoFileName(courseBindingModel.getName(), videoToUpload.getKey(), videoToUpload.getValue())))
                .collect(Collectors.toList());
    }

    private String getThumbnailName(CourseBindingModel courseBindingModel) {
        String thumbnailFileExtension = FilenameUtils.getExtension(courseBindingModel.getThumbnail().getOriginalFilename());
        return courseBindingModel.getName() + THUMBNAIL_FILE_EXTENSION + "." + thumbnailFileExtension;
    }

    @Override
    public ResponseEntity<List<CourseResponseModel>> getLatest(int count, boolean loadThumbnails) {
        List<CourseServiceModel> courseServiceModels = courseService.findLatestCourses(count);
        return ResponseEntity.ok(toCourseResponseModels(courseServiceModels, loadThumbnails));
    }

    private List<CourseResponseModel> toCourseResponseModels(List<CourseServiceModel> courseServiceModels, boolean loadThumbnails) {
        List<Future<CourseResponseModel>> courseResponseModelTasks = new ArrayList<>();
        if (!loadThumbnails) {
            return toOutput(courseServiceModels);
        }
        for (CourseServiceModel courseServiceModel : courseServiceModels) {
            Future<CourseResponseModel> courseResponseModelTask = executorService.submit(() -> toCourseResponseModel(courseServiceModel));
            courseResponseModelTasks.add(courseResponseModelTask);
        }
        return waitCourseResponseModelTasksToBeExecuted(courseResponseModelTasks);
    }

    private CourseResponseModel toCourseResponseModel(CourseServiceModel courseServiceModel) {
        CourseResponseModel courseResponseModel = toOutput(courseServiceModel);
        byte[] thumbnailContent = remoteStorageFileGetter.getImageResource(courseServiceModel.getThumbnailName());
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
    public ResponseEntity<List<CourseResponseModel>> getByPageNameCategory(int page, String name, String category) {
        List<CourseServiceModel> courseServiceModels = courseService.findCourses(page, configuration.getMaxCoursesOnPage(), name, category);
        return ResponseEntity.ok(toCourseResponseModels(courseServiceModels, true));
    }

    @Override
    public ResponseEntity<CourseResponseModel> get(String name) {
        CourseServiceModel courseServiceModel = courseService.findByName(name);
        return ResponseEntity.ok(toOutput(courseServiceModel));
    }

    @Override
    public ResponseEntity<CoursePagesResponseModel> getPagesCount(String courseName) {
        if (courseName == null) {
            long coursesCount = courseService.count();
            long pages = coursesCount / configuration.getMaxCoursesOnPage();
            if (coursesCount % configuration.getMaxCoursesOnPage() != 0) {
                pages++;
            }
            return ResponseEntity.ok(new CoursePagesResponseModel(pages));
        }
        long coursesCount = courseService.countByNameLike(courseName);
        long pages = coursesCount / configuration.getMaxCoursesOnPage();
        if (coursesCount % configuration.getMaxCoursesOnPage() != 0) {
            pages++;
        }
        return ResponseEntity.ok(new CoursePagesResponseModel(pages));
    }

    @Override
    public ResponseEntity<CourseResponseModel> enrollUserForCourse(String username, String courseName) {
        CourseServiceModel courseServiceModel = courseService.enrollUserForCourse(username, courseName);
        return ResponseEntity.ok(toOutput(courseServiceModel));
    }

    @Override
    public ResponseEntity<UserEnrolledForCourse> isUserEnrolledForCourse(String username, String courseName) {
        UserServiceModel userServiceModel = userService.findByUsername(username);
        UserEnrolledForCourse userEnrolledForCourse = new UserEnrolledForCourse();
        userEnrolledForCourse.setUserEnrolledForCourse(isUserEnrolledForCourse(userServiceModel, courseName));
        return ResponseEntity.ok(userEnrolledForCourse);
    }

    private boolean isUserEnrolledForCourse(UserServiceModel userServiceModel, String courseName) {
        return userServiceModel.getCourses()
                .stream()
                .map(CourseServiceModel::getName)
                .anyMatch(courseServiceModelName -> Objects.equals(courseServiceModelName, courseName));
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
