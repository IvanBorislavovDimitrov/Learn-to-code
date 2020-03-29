package com.code.to.learn.persistence.dao.impl;

import com.code.to.learn.persistence.dao.api.NamedElementDao;
import com.code.to.learn.persistence.domain.entity.IdEntity;
import com.code.to.learn.persistence.domain.generic.NamedElement;
import org.hibernate.SessionFactory;

import java.util.Optional;

public abstract class NamedElementDaoImpl<E extends IdEntity<E> & NamedElement> extends GenericDaoImpl<E> implements NamedElementDao<E> {

    public NamedElementDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Optional<E> findByName(String name) {
        return super.findByField(NamedElement.NAME, name);
    }

}
