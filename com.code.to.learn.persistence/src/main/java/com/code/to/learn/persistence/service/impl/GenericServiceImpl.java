package com.code.to.learn.persistence.service.impl;

import com.code.to.learn.persistence.dao.api.GenericDao;
import com.code.to.learn.persistence.domain.entity.IdEntity;
import com.code.to.learn.persistence.domain.model.IdServiceModel;
import com.code.to.learn.persistence.exception.IdNotFoundException;
import com.code.to.learn.persistence.hibernate.HibernateUtils;
import com.code.to.learn.persistence.service.api.GenericService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public abstract class GenericServiceImpl<E extends IdEntity, M extends IdServiceModel> implements GenericService<M> {

    protected final ModelMapper modelMapper;
    private final GenericDao<E> genericDao;

    protected GenericServiceImpl(GenericDao<E> genericDao, ModelMapper modelMapper) {
        this.genericDao = genericDao;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<M> findAll() {
        SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            return genericDao.findAll(session).stream()
                    .map(idServiceEntity -> modelMapper.map(idServiceEntity, getModelClass()))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public M findById(String id) {
        SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            E entity = genericDao.findById(id, session).orElseThrow(() -> new IdNotFoundException(id));
            return modelMapper.map(entity, getModelClass());
        }
    }

    @Override
    public void save(M model) {
        SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            E entity = modelMapper.map(model, getEntityClass());
            genericDao.persist(entity, session);
        }
    }

    @Override
    public M deleteById(String id) {
        SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            E entity = genericDao.deleteById(id, session).orElseThrow(() -> new IdNotFoundException(id));
            return modelMapper.map(entity, getModelClass());
        }
    }

    @Override
    public M delete(M model) {
        return deleteById(model.getId());
    }

    @Override
    public M update(M model) {
        SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            E entity = modelMapper.map(model, getEntityClass());
            return modelMapper.map(genericDao.update(entity, session).orElseThrow(() -> new IdNotFoundException(entity.getId())),
                    getModelClass());
        }
    }

    protected abstract Class<M> getModelClass();

    protected abstract Class<E> getEntityClass();
}
