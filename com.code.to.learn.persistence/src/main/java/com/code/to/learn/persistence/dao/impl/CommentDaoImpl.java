package com.code.to.learn.persistence.dao.impl;

import com.code.to.learn.persistence.dao.api.CommentDao;
import com.code.to.learn.persistence.domain.entity.Comment;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CommentDaoImpl extends GenericDaoImpl<Comment> implements CommentDao {

    @Autowired
    public CommentDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Class<Comment> getDomainClassType() {
        return Comment.class;
    }
}
