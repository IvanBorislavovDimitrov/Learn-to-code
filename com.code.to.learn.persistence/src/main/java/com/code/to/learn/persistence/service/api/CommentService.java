package com.code.to.learn.persistence.service.api;

import com.code.to.learn.persistence.domain.model.CommentServiceModel;

import java.util.List;

public interface CommentService extends GenericService<CommentServiceModel> {

    List<CommentServiceModel> findCommentsByCourseName(String courseName);
}
