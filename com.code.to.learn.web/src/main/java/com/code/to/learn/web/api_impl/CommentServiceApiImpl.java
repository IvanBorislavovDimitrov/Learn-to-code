package com.code.to.learn.web.api_impl;

import com.code.to.learn.api.api.comment.CommentServiceApi;
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

@Service
public class CommentServiceApiImpl extends ExtendableMapper<CommentServiceModel, CommentResponseModel> implements CommentServiceApi {

    private final CommentService commentService;
    private final CourseService courseService;
    private final UserService userService;

    @Autowired
    public CommentServiceApiImpl(ModelMapper modelMapper, CommentService commentService, CourseService courseService, UserService userService) {
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

    private CommentServiceModel toCourseServiceModel(CommentBindingModel commentBindingModel) {
        CommentServiceModel commentServiceModel = getMapper().map(commentBindingModel, CommentServiceModel.class);
        commentServiceModel.setAuthor(userService.findByUsername(commentBindingModel.getAuthor()));
        commentServiceModel.setCourse(courseService.findByName(commentBindingModel.getCourseName()));
        commentServiceModel.setDate(LocalDate.now());
        return commentServiceModel;
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
