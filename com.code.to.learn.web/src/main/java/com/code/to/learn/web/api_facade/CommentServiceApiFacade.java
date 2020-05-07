package com.code.to.learn.web.api_facade;

import com.code.to.learn.api.api.comment.CommentServiceApi;
import com.code.to.learn.api.exception.ForbiddenException;
import com.code.to.learn.api.model.comment.CommentBindingModel;
import com.code.to.learn.api.model.comment.CommentResponseModel;
import com.code.to.learn.persistence.domain.model.CommentServiceModel;
import com.code.to.learn.persistence.service.api.CommentService;
import com.code.to.learn.persistence.service.api.CourseService;
import com.code.to.learn.persistence.service.api.UserService;
import com.code.to.learn.util.mapper.ExtendableMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static com.code.to.learn.web.constants.Messages.USERS_DO_NOT_MATCH;
import static com.code.to.learn.web.constants.Messages.USER_CANNOT_NOT_MODIFY_COMMENT;


@Service
public class CommentServiceApiFacade extends ExtendableMapper<CommentServiceModel, CommentResponseModel> implements CommentServiceApi {

    private final CommentService commentService;
    private final CourseService courseService;
    private final UserService userService;

    @Autowired
    public CommentServiceApiFacade(ModelMapper modelMapper, CommentService commentService, CourseService courseService, UserService userService) {
        super(modelMapper);
        this.commentService = commentService;
        this.courseService = courseService;
        this.userService = userService;
    }

    @Override
    public ResponseEntity<CommentResponseModel> add(CommentBindingModel commentBindingModel) {
        CommentServiceModel courseServiceModel = toCourseServiceModel(commentBindingModel);
        commentService.save(courseServiceModel);
        return ResponseEntity.ok(toOutput(courseServiceModel));
    }

    @Override
    public ResponseEntity<List<CommentResponseModel>> getCommentsByCourseName(String courseName) {
        return ResponseEntity.ok(toOutput(commentService.findCommentsByCourseName(courseName)));
    }

    private CommentServiceModel toCourseServiceModel(CommentBindingModel commentBindingModel) {
        CommentServiceModel commentServiceModel = getMapper().map(commentBindingModel, CommentServiceModel.class);
        commentServiceModel.setAuthor(userService.findByUsername(commentBindingModel.getAuthor()));
        commentServiceModel.setCourse(courseService.findByName(commentBindingModel.getCourseName()));
        commentServiceModel.setDate(LocalDate.now());
        return commentServiceModel;
    }

    @Override
    public ResponseEntity<CommentResponseModel> update(CommentBindingModel commentBindingModel, String loggedUser) {
        if (!Objects.equals(commentBindingModel.getAuthor(), loggedUser)) {
            throw new ForbiddenException(USERS_DO_NOT_MATCH);
        }
        CommentServiceModel currentCommentServiceModel = commentService.findById(commentBindingModel.getId());
        CommentServiceModel commentToUpdate = getUpdatedCommentServiceModel(currentCommentServiceModel, commentBindingModel);
        return ResponseEntity.ok(toOutput(commentService.update(commentToUpdate)));
    }

    private CommentServiceModel getUpdatedCommentServiceModel(CommentServiceModel currentCommentServiceModel,
                                                              CommentBindingModel commentBindingModel) {
        currentCommentServiceModel.setContent(commentBindingModel.getContent());
        return currentCommentServiceModel;
    }

    @Override
    public ResponseEntity<CommentResponseModel> delete(String commentId, String loggedUser) {
        CommentServiceModel commentServiceModel = commentService.findById(commentId);
        if (!Objects.equals(commentServiceModel.getAuthor().getUsername(), loggedUser)
                && !commentServiceModel.getAuthor().isAdminOrModerator()) {
            throw new ForbiddenException(USER_CANNOT_NOT_MODIFY_COMMENT);
        }
        CommentServiceModel deletedCourseServiceModel = commentService.deleteById(commentId);
        return ResponseEntity.ok(toOutput(deletedCourseServiceModel));
    }

    @Override
    public ResponseEntity<CommentResponseModel> get(String commentId) {
        return ResponseEntity.ok(toOutput(commentService.findById(commentId)));
    }

    @Override
    protected Class<CommentServiceModel> getInputClass() {
        return CommentServiceModel.class;
    }

    @Override
    protected Class<CommentResponseModel> getOutputClass() {
        return CommentResponseModel.class;
    }
}
