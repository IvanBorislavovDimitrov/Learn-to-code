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
import com.code.to.learn.persistence.exception.basic.NotFoundException;
import com.code.to.learn.persistence.service.api.CourseCategoryService;
import com.code.to.learn.persistence.service.api.CourseService;
import com.code.to.learn.persistence.service.api.UserService;
import com.code.to.learn.util.mapper.ExtendableMapper;
import com.code.to.learn.web.util.FileToUpload;
import com.code.to.learn.web.util.RemoteStorageFileGetter;
import com.code.to.learn.web.util.RemoteStorageFileOperator;
import com.dropbox.core.v2.files.FileMetadata;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static com.code.to.learn.api.constant.Constants.DATE_PATTERN;
import static com.code.to.learn.web.constants.Constants.*;
import static com.code.to.learn.web.constants.Messages.VIDEO_NAME_NOT_FOUND;

@Component
public class CourseServiceApiImpl extends ExtendableMapper<CourseServiceModel, CourseResponseModel> implements CourseServiceApi {

    private final CourseService courseService;
    private final RemoteStorageFileOperator remoteStorageFileOperator;
    private final CourseValidator courseValidator;
    private final UserService userService;
    private final CourseCategoryService courseCategoryService;
    private final ExecutorService executorService;
    private final RemoteStorageFileGetter remoteStorageFileGetter;
    private final ApplicationConfiguration configuration;

    @Autowired
    public CourseServiceApiImpl(CourseService courseService, ModelMapper modelMapper, RemoteStorageFileOperator remoteStorageFileOperator,
                                CourseValidator courseValidator, UserService userService,
                                CourseCategoryService courseCategoryService, ExecutorService executorService,
                                RemoteStorageFileGetter remoteStorageFileGetter, ApplicationConfiguration configuration) {
        super(modelMapper);
        this.courseService = courseService;
        this.remoteStorageFileOperator = remoteStorageFileOperator;
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
        List<FileToUpload> videosToUpload = getVideosToUpload(courseBindingModel);
        List<FileMetadata> uploadedVideos = remoteStorageFileOperator.uploadFilesSync(videosToUpload);
        CourseServiceModel courseServiceModel = toCourseServiceModel(courseBindingModel, uploadedVideos, true);
        FileToUpload thumbnail = new FileToUpload(getThumbnailName(courseBindingModel), courseBindingModel.getThumbnail());
        remoteStorageFileOperator.uploadFileAsync(thumbnail);
        courseService.save(courseServiceModel);
        return ResponseEntity.ok(toOutput(courseServiceModel));
    }

    private CourseServiceModel toCourseServiceModel(CourseBindingModel courseBindingModel, List<FileMetadata> uploadedVideos,
                                                    boolean shouldUpdateContent) {
        CourseServiceModel courseServiceModel = getMapper().map(courseBindingModel, CourseServiceModel.class);
        if (shouldUpdateContent) {
            List<CourseServiceModel.CourseVideoServiceModel> videos = getVideos(courseBindingModel, uploadedVideos);
            if (!videos.isEmpty()) {
                courseServiceModel.setVideosNames(videos);
            }
        }
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

    private List<FileToUpload> getVideosToUpload(CourseBindingModel courseBindingModel) {
        List<FileToUpload> videosToUpload = new ArrayList<>();
        courseBindingModel.getVideosToUpload().forEach((videoName, videoFile) -> {
            FileToUpload fileToUpload = new FileToUpload(getVideoFileName(courseBindingModel.getName(), videoName, videoFile), videoFile);
            videosToUpload.add(fileToUpload);
        });
        return videosToUpload;
    }

    private String getVideoFileName(String courseName, String videoName, MultipartFile videoFile) {
        String videoFileExtension = FilenameUtils.getExtension(videoFile.getOriginalFilename());
        return courseName + DELIMITER + videoName + COURSE_VIDEO_EXTENSION + "." + videoFileExtension;
    }

    private List<CourseServiceModel.CourseVideoServiceModel> getVideos(CourseBindingModel courseBindingModel, List<FileMetadata> fileMetadata) {
        return courseBindingModel.getVideosToUpload().entrySet()
                .stream()
                .map(videoToUpload -> {
                    String videoTitle = videoToUpload.getKey();
                    String videoFullName = getVideoFileName(courseBindingModel.getName(), videoToUpload.getKey(), videoToUpload.getValue());
                    long videoFileSize = getVideoFileSize(videoFullName, fileMetadata);
                    return new CourseServiceModel.CourseVideoServiceModel(videoTitle, videoFullName, videoFileSize);
                })
                .collect(Collectors.toList());
    }

    private long getVideoFileSize(String videoName, List<FileMetadata> fileMetadata) {
        return fileMetadata.stream()
                .filter(metadata -> Objects.equals(videoName, metadata.getName()))
                .map(FileMetadata::getSize)
                .findFirst()
                .orElseThrow(() -> new NotFoundException(VIDEO_NAME_NOT_FOUND, videoName));
    }

    private String getThumbnailName(CourseBindingModel courseBindingModel) {
        if (courseBindingModel.getThumbnail() == null) {
            return null;
        }
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
    public ResponseEntity<CourseResponseModel> addToCart(String username, String courseName) {
        CourseServiceModel courseServiceModel = courseService.addToCart(username, courseName);
        return ResponseEntity.ok(toOutput(courseServiceModel));
    }

    @Override
    public ResponseEntity<List<CourseResponseModel>> getCoursesInCart(String username) {
        List<CourseServiceModel> coursesInCart = courseService.getCoursesInCart(username);
        return ResponseEntity.ok(toOutput(coursesInCart));
    }

    @Override
    public ResponseEntity<CourseResponseModel> removeCourseFromCart(String username, String courseName) {
        CourseServiceModel courseServiceModel = courseService.removeCourseFromCart(username, courseName);
        return ResponseEntity.ok(toOutput(courseServiceModel));
    }

    @Override
    public ResponseEntity<List<CourseResponseModel>> enrollFromCart(String username) {
        UserServiceModel userServiceModel = userService.findByUsername(username);
        List<CourseServiceModel> enrolledCourses = enrollUserForAllCoursesInCart(userServiceModel);
        courseService.emptyCart(username);
        return ResponseEntity.ok(toOutput(enrolledCourses));
    }

    private List<CourseServiceModel> enrollUserForAllCoursesInCart(UserServiceModel userServiceModel) {
        List<CourseServiceModel> enrolledCourses = new ArrayList<>();
        for (String courseName : getCoursesToEnroll(userServiceModel.getCoursesInCart())) {
            CourseServiceModel courseServiceModel = courseService.enrollUserForCourse(userServiceModel.getUsername(), courseName);
            enrolledCourses.add(courseServiceModel);
        }
        return enrolledCourses;
    }

    private List<String> getCoursesToEnroll(List<CourseServiceModel> courseServiceModels) {
        return courseServiceModels.stream()
                .map(CourseServiceModel::getName)
                .collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<CourseResponseModel.CourseVideoResponseModel> getVideoByCourse(String courseName, String videoName) {
        CourseServiceModel courseServiceModel = courseService.findByName(courseName);
        List<CourseServiceModel.CourseVideoServiceModel> videosNames = courseServiceModel.getVideosNames();
        return ResponseEntity.ok(getMapper().map(getVideoByName(videosNames, videoName), CourseResponseModel.CourseVideoResponseModel.class));
    }

    private CourseServiceModel.CourseVideoServiceModel getVideoByName(List<CourseServiceModel.CourseVideoServiceModel> videos, String videoName) {
        return videos.stream()
                .filter(video -> Objects.equals(video.getVideoFullName(), videoName))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(VIDEO_NAME_NOT_FOUND, videoName));
    }

    @Override
    public ResponseEntity<CourseResponseModel> updateCourse(CourseBindingModel courseBindingModel, boolean shouldUpdateCourse) {
        CourseServiceModel currentCourseServiceModel = courseService.findByName(courseBindingModel.getName());
        List<FileMetadata> updatedVideos = updateCourseVideos(shouldUpdateCourse, courseBindingModel, currentCourseServiceModel);
        if (shouldUpdateCourse) {
            remoteStorageFileOperator.removeFileSync(currentCourseServiceModel.getThumbnailName());
            FileToUpload thumbnail = new FileToUpload(getThumbnailName(courseBindingModel), courseBindingModel.getThumbnail());
            remoteStorageFileOperator.uploadFileSync(thumbnail);
        }
        CourseServiceModel updatedCourseServiceModel = toCourseServiceModel(courseBindingModel, updatedVideos, shouldUpdateCourse);
        updatedCourseServiceModel = updateCourseServiceModel(currentCourseServiceModel, updatedCourseServiceModel);
        courseService.update(updatedCourseServiceModel);
        return ResponseEntity.ok(toOutput(updatedCourseServiceModel));
    }

    private List<FileMetadata> updateCourseVideos(boolean shouldUpdateCourse, CourseBindingModel courseBindingModel,
                                                  CourseServiceModel currentCourseServiceModel) {
        if (!shouldUpdateCourse) {
            return Collections.emptyList();
        }
        return updateVideoContent(courseBindingModel, currentCourseServiceModel);
    }

    private List<FileMetadata> updateVideoContent(CourseBindingModel courseBindingModel, CourseServiceModel currentCourseServiceModel) {
        List<CourseServiceModel.CourseVideoServiceModel> currentVideos = currentCourseServiceModel.getVideosNames();
        removeVideos(currentVideos);
        List<FileToUpload> videosToUpload = getVideosToUpload(courseBindingModel);
        return remoteStorageFileOperator.uploadFilesSync(videosToUpload);
    }

    private void removeVideos(List<CourseServiceModel.CourseVideoServiceModel> videos) {
        videos.stream()
                .map(CourseServiceModel.CourseVideoServiceModel::getVideoFullName)
                .forEach(remoteStorageFileOperator::removeFileSync);
    }

    private CourseServiceModel updateCourseServiceModel(CourseServiceModel currentCourseServiceModel, CourseServiceModel courseServiceModelToUpdate) {
        courseServiceModelToUpdate.setAttendants(currentCourseServiceModel.getAttendants());
        courseServiceModelToUpdate.setFutureAttendants(currentCourseServiceModel.getFutureAttendants());
        courseServiceModelToUpdate.setId(currentCourseServiceModel.getId());
        courseServiceModelToUpdate.setHomework(currentCourseServiceModel.getHomework());
        if (courseServiceModelToUpdate.getVideosNames() == null) {
            courseServiceModelToUpdate.setVideosNames(currentCourseServiceModel.getVideosNames());
        }
        if (courseServiceModelToUpdate.getThumbnailName() == null) {
            courseServiceModelToUpdate.setThumbnailName(currentCourseServiceModel.getThumbnailName());
        }
        return courseServiceModelToUpdate;
    }

    @Override
    public ResponseEntity<CourseResponseModel> deleteCourse(String courseName) {
        CourseServiceModel courseServiceModel = courseService.findByName(courseName);
        CourseServiceModel deletedCourseServiceModel = courseService.deleteById(courseServiceModel.getId());
        return ResponseEntity.ok(toOutput(deletedCourseServiceModel));
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
