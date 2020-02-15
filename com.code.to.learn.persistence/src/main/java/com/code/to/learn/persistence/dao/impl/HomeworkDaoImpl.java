package com.code.to.learn.persistence.dao.impl;

import com.code.to.learn.persistence.domain.entity.Homework;
import com.code.to.learn.persistence.dao.api.HomeworkDao;
import org.springframework.stereotype.Repository;

@Repository
public class HomeworkDaoImpl extends GenericDaoImpl<Homework> implements HomeworkDao {

    @Override
    protected Class<Homework> getDomainClassType() {
        return Homework.class;
    }
}
