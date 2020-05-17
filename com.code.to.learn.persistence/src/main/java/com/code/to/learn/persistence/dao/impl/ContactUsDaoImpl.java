package com.code.to.learn.persistence.dao.impl;

import com.code.to.learn.persistence.dao.api.ContactUsDao;
import com.code.to.learn.persistence.domain.entity.ContactUs;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ContactUsDaoImpl extends GenericDaoImpl<ContactUs> implements ContactUsDao {

    @Autowired
    public ContactUsDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    protected Class<ContactUs> getDomainClassType() {
        return ContactUs.class;
    }
}
