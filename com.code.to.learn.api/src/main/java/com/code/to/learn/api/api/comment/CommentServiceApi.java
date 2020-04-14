package com.code.to.learn.api.api.comment;

import com.code.to.learn.api.model.comment.CommentBindingModel;
import com.code.to.learn.api.model.comment.CommentResponseModel;
import org.springframework.http.ResponseEntity;

public interface CommentServiceApi {

    ResponseEntity<CommentResponseModel> add(CommentBindingModel commentBindingModel);
}
