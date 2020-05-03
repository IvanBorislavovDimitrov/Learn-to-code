package com.code.to.learn.api.api.comment;

import com.code.to.learn.api.model.comment.CommentBindingModel;
import com.code.to.learn.api.model.comment.CommentResponseModel;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CommentServiceApi {

    ResponseEntity<CommentResponseModel> add(CommentBindingModel commentBindingModel);

    ResponseEntity<List<CommentResponseModel>> getCommentsByCourseName(String courseName);

    ResponseEntity<CommentResponseModel> update(CommentBindingModel commentBindingModel, String loggedUser);

    ResponseEntity<CommentResponseModel> delete(String commentId, String loggedUser);
}
