package com.code.to.learn.persistence.repository.impl;

import com.code.to.learn.persistence.domain.entity.Homework;
import com.code.to.learn.persistence.repository.api.HomeworkRepository;
import org.springframework.stereotype.Repository;

@Repository
public class HomeworkRepositoryImpl extends GenericRepositoryImpl<Homework> implements HomeworkRepository {

    @Override
    protected Class<Homework> getDomainClassType() {
        return Homework.class;
    }
}
