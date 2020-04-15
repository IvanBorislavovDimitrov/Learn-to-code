package com.code.to.learn.persistence.service.impl;

import com.code.to.learn.persistence.dao.api.CommentDao;
import com.code.to.learn.persistence.domain.entity.Comment;
import com.code.to.learn.persistence.domain.model.CommentServiceModel;
import com.code.to.learn.persistence.service.api.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl extends GenericServiceImpl<Comment, CommentServiceModel> implements CommentService {

    private final CommentDao commentDao;

    @Autowired
    protected CommentServiceImpl(CommentDao commentDao, ModelMapper modelMapper) {
        super(commentDao, modelMapper);
        this.commentDao = commentDao;
    }

    @Override
    public List<CommentServiceModel> findCommentsByCourseName(String courseName) {
        return toOutput(commentDao.findAllByCourseName(courseName));
    }

    @Override
    protected Class<Comment> getEntityClass() {
        return Comment.class;
    }

    @Override
    protected Class<CommentServiceModel> getModelClass() {
        return CommentServiceModel.class;
    }
}
