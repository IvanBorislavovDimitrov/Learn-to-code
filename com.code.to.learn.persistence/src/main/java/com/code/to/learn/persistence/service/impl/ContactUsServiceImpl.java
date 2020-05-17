package com.code.to.learn.persistence.service.impl;

import com.code.to.learn.persistence.dao.api.GenericDao;
import com.code.to.learn.persistence.domain.entity.ContactUs;
import com.code.to.learn.persistence.domain.model.ContactUsServiceModel;
import com.code.to.learn.persistence.service.api.ContactUsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ContactUsServiceImpl extends GenericServiceImpl<ContactUs, ContactUsServiceModel> implements ContactUsService {

    @Autowired
    protected ContactUsServiceImpl(GenericDao<ContactUs> genericDao, ModelMapper modelMapper) {
        super(genericDao, modelMapper);
    }

    @Override
    protected Class<ContactUs> getEntityClass() {
        return ContactUs.class;
    }

    @Override
    protected Class<ContactUsServiceModel> getModelClass() {
        return ContactUsServiceModel.class;
    }
}
