package com.code.to.learn.persistence.dao.impl;

import com.code.to.learn.persistence.dao.api.HomeworkDao;
import com.code.to.learn.persistence.domain.entity.Homework;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class HomeworkDaoImpl extends GenericDaoImpl<Homework> implements HomeworkDao {

    @Autowired
    public HomeworkDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    protected Class<Homework> getDomainClassType() {
        return Homework.class;
    }
}
