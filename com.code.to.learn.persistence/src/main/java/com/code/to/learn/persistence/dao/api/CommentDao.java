package com.code.to.learn.persistence.dao.api;

import com.code.to.learn.persistence.domain.entity.Comment;

import java.util.List;

public interface CommentDao extends GenericDao<Comment> {

    List<Comment> findAllByCourseName(String courseName);
}
