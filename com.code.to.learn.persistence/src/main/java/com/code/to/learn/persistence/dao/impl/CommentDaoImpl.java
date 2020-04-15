package com.code.to.learn.persistence.dao.impl;

import com.code.to.learn.persistence.dao.api.CommentDao;
import com.code.to.learn.persistence.domain.entity.Comment;
import com.code.to.learn.persistence.domain.entity.Course;
import com.code.to.learn.persistence.util.DatabaseSessionUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.*;
import java.util.List;

@Repository
public class CommentDaoImpl extends GenericDaoImpl<Comment> implements CommentDao {

    @Autowired
    public CommentDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public List<Comment> findAllByCourseName(String courseName) {
        Session session = DatabaseSessionUtil.getCurrentOrOpen(getSessionFactory());
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Comment> commentCriteriaQuery = criteriaBuilder.createQuery(Comment.class);
        Root<Comment> commentRoot = commentCriteriaQuery.from(Comment.class);
        Join<Object, Object> commentCourseInnerJoin = commentRoot.join(Comment.COURSE, JoinType.INNER);
        commentCriteriaQuery.where(criteriaBuilder.equal(commentCourseInnerJoin.get(Course.NAME), courseName));
        commentCriteriaQuery.select(commentRoot);
        Query<Comment> commentQuery = session.createQuery(commentCriteriaQuery);
        return commentQuery.getResultList();
    }

    @Override
    public Class<Comment> getDomainClassType() {
        return Comment.class;
    }
}
