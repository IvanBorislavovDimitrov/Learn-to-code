package com.code.to.learn.persistence.service.api;

import com.code.to.learn.persistence.domain.entity.Homework;
import com.code.to.learn.persistence.domain.model.HomeworkServiceModel;

public interface HomeworkService extends GenericService<HomeworkServiceModel>, NamedElementService<Homework, HomeworkServiceModel> {
}
