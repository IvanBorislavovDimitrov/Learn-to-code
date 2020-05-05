package com.code.to.learn.web.api_facade;

import com.code.to.learn.api.api.course_category.CourseCategoryServiceApi;
import com.code.to.learn.api.model.course_category.CourseCategoryBindingModel;
import com.code.to.learn.api.model.course_category.CourseCategoryResponseModel;
import com.code.to.learn.persistence.domain.model.CourseCategoryServiceModel;
import com.code.to.learn.persistence.domain.model.CourseCategoryWithCoursesNumber;
import com.code.to.learn.persistence.service.api.CourseCategoryService;
import com.code.to.learn.util.mapper.ExtendableMapper;
import com.code.to.learn.web.util.FileToUpload;
import com.code.to.learn.web.util.RemoteStorageFileOperator;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.code.to.learn.web.constants.Constants.COURSE_CATEGORY_THUMBNAIL;

@Service
public class CourseCategoryServiceApiFacade extends ExtendableMapper<CourseCategoryServiceModel, CourseCategoryResponseModel> implements CourseCategoryServiceApi {

    private final CourseCategoryService courseCategoryService;
    private final RemoteStorageFileOperator remoteStorageFileOperator;

    @Autowired
    public CourseCategoryServiceApiFacade(ModelMapper modelMapper, CourseCategoryService courseCategoryService, RemoteStorageFileOperator remoteStorageFileOperator) {
        super(modelMapper);
        this.courseCategoryService = courseCategoryService;
        this.remoteStorageFileOperator = remoteStorageFileOperator;
    }

    @Override
    public ResponseEntity<CourseCategoryResponseModel> add(CourseCategoryBindingModel courseCategoryBindingModel) {
        CourseCategoryServiceModel courseCategoryServiceModel = toCourseCategoryServiceModel(courseCategoryBindingModel);
        uploadCourseCategoryThumbnail(courseCategoryBindingModel);
        courseCategoryService.save(courseCategoryServiceModel);
        CourseCategoryResponseModel courseCategoryResponseModel = toOutput(courseCategoryServiceModel);
        return ResponseEntity.ok(courseCategoryResponseModel);
    }

    private CourseCategoryServiceModel toCourseCategoryServiceModel(CourseCategoryBindingModel courseCategoryBindingModel) {
        CourseCategoryServiceModel courseCategoryServiceModel = getMapper().map(courseCategoryBindingModel, CourseCategoryServiceModel.class);
        courseCategoryServiceModel.setThumbnailName(getCourseCategoryFileToUploadName(courseCategoryBindingModel));
        return courseCategoryServiceModel;
    }

    private void uploadCourseCategoryThumbnail(CourseCategoryBindingModel courseCategoryBindingModel) {
        String courseCategoryFileToUploadName = getCourseCategoryFileToUploadName(courseCategoryBindingModel);
        FileToUpload fileToUpload = new FileToUpload(courseCategoryFileToUploadName, courseCategoryBindingModel.getThumbnail());
        remoteStorageFileOperator.uploadFileAsync(fileToUpload);
    }

    private String getCourseCategoryFileToUploadName(CourseCategoryBindingModel courseCategoryBindingModel) {
        String courseCategoryThumbnailFileExtension = FilenameUtils.getExtension(courseCategoryBindingModel.getThumbnail().getOriginalFilename());
        return courseCategoryBindingModel.getName() + COURSE_CATEGORY_THUMBNAIL + "." + courseCategoryThumbnailFileExtension;
    }

    @Override
    public ResponseEntity<CourseCategoryResponseModel> update(CourseCategoryBindingModel courseCategoryBindingModel) {
        CourseCategoryServiceModel currentCourseCategoryServiceModel = courseCategoryService.findByName(courseCategoryBindingModel.getName());
        removeOldCourseCategoryThumbnail(currentCourseCategoryServiceModel.getThumbnailName());
        CourseCategoryServiceModel courseCategoryServiceModel = updateCourseServiceModel(courseCategoryBindingModel, currentCourseCategoryServiceModel);
        uploadCourseCategoryThumbnail(courseCategoryBindingModel);
        CourseCategoryServiceModel updatedCourseCategoryServiceModel = courseCategoryService.update(courseCategoryServiceModel);
        return ResponseEntity.ok(toOutput(updatedCourseCategoryServiceModel));
    }

    private void removeOldCourseCategoryThumbnail(String courseCategoryThumbnailName) {
        if (courseCategoryThumbnailName == null) {
            return;
        }
        remoteStorageFileOperator.removeFileSync(courseCategoryThumbnailName);
    }

    private CourseCategoryServiceModel updateCourseServiceModel(CourseCategoryBindingModel courseCategoryBindingModel,
                                                                CourseCategoryServiceModel currentCourseCategoryServiceModel) {
        currentCourseCategoryServiceModel.setThumbnailName(getCourseCategoryFileToUploadName(courseCategoryBindingModel));
        currentCourseCategoryServiceModel.setDescription(courseCategoryBindingModel.getDescription());
        return currentCourseCategoryServiceModel;
    }

    @Override
    public ResponseEntity<List<CourseCategoryResponseModel>> getCourseCategories() {
        List<CourseCategoryServiceModel> categoryServiceModels = courseCategoryService.findAll();
        return ResponseEntity.ok(toOutput(categoryServiceModels));
    }

    @Override
    public ResponseEntity<List<CourseCategoryResponseModel>> getCategoriesWithMostCourses(int limit) {
        List<CourseCategoryWithCoursesNumber> categoriesWithMostCourses = courseCategoryService.findCategoriesWithMostCourses();
        return ResponseEntity.ok(toLimitedCourseCategoryResponseModels(categoriesWithMostCourses, limit));
    }

    private List<CourseCategoryResponseModel> toLimitedCourseCategoryResponseModels(List<CourseCategoryWithCoursesNumber> categories,
                                                                                    int limit) {
        return categories.stream()
                .map(course -> getMapper().map(course, CourseCategoryResponseModel.class))
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<CourseCategoryResponseModel> getByName(String courseCategoryName) {
        CourseCategoryServiceModel courseCategoryServiceModel = courseCategoryService.findByName(courseCategoryName);
        return ResponseEntity.ok(toOutput(courseCategoryServiceModel));
    }

    @Override
    protected Class<CourseCategoryServiceModel> getInputClass() {
        return CourseCategoryServiceModel.class;
    }

    @Override
    protected Class<CourseCategoryResponseModel> getOutputClass() {
        return CourseCategoryResponseModel.class;
    }
}
