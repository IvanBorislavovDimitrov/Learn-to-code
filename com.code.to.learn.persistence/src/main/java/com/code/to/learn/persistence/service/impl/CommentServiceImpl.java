package com.code.to.learn.persistence.service.impl;

import com.code.to.learn.persistence.dao.api.CommentDao;
import com.code.to.learn.persistence.domain.entity.Comment;
import com.code.to.learn.persistence.domain.model.CommentServiceModel;
import com.code.to.learn.persistence.service.api.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl extends GenericServiceImpl<Comment, CommentServiceModel> implements CommentService {

    @Autowired
    protected CommentServiceImpl(CommentDao commentDao, ModelMapper modelMapper) {
        super(commentDao, modelMapper);
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
