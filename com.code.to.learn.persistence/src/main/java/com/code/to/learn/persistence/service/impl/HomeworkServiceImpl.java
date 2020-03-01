package com.code.to.learn.persistence.service.impl;

import com.code.to.learn.persistence.dao.api.HomeworkDao;
import com.code.to.learn.persistence.domain.entity.Homework;
import com.code.to.learn.persistence.domain.model.HomeworkServiceModel;
import com.code.to.learn.persistence.service.api.HomeworkService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HomeworkServiceImpl extends GenericServiceImpl<Homework, HomeworkServiceModel> implements HomeworkService {

    @Autowired
    public HomeworkServiceImpl(HomeworkDao homeworkDao, ModelMapper modelMapper) {
        super(homeworkDao, modelMapper);
    }

    @Override
    protected Class<HomeworkServiceModel> getModelClass() {
        return HomeworkServiceModel.class;
    }

    @Override
    protected Class<Homework> getEntityClass() {
        return Homework.class;
    }
}
