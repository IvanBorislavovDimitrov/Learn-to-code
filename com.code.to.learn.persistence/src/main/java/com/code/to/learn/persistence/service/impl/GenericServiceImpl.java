package com.code.to.learn.persistence.service.impl;

import com.code.to.learn.persistence.dao.api.GenericDao;
import com.code.to.learn.persistence.domain.entity.IdEntity;
import com.code.to.learn.persistence.domain.model.IdServiceModel;
import com.code.to.learn.persistence.exception.IdNotFoundException;
import com.code.to.learn.persistence.service.api.GenericService;
import com.code.to.learn.util.mapper.ExtendableMapper;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public abstract class GenericServiceImpl<E extends IdEntity, M extends IdServiceModel> extends ExtendableMapper<E, M> implements GenericService<M> {

    protected final ModelMapper modelMapper;
    private final GenericDao<E> genericDao;

    protected GenericServiceImpl(GenericDao<E> genericDao, ModelMapper modelMapper) {
        super(modelMapper);
        this.genericDao = genericDao;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<M> findAll() {
        return genericDao.findAll().stream()
                .map(idServiceEntity -> modelMapper.map(idServiceEntity, getModelClass()))
                .collect(Collectors.toList());
    }

    @Override
    public M findById(String id) {
        E entity = genericDao.findById(id).orElseThrow(() -> new IdNotFoundException(id));
        return modelMapper.map(entity, getModelClass());
    }

    @Override
    public void save(M model) {
        E entity = modelMapper.map(model, getEntityClass());
        genericDao.persist(entity);
    }

    @Override
    public M deleteById(String id) {
        E entity = genericDao.deleteById(id).orElseThrow(() -> new IdNotFoundException(id));
        return modelMapper.map(entity, getModelClass());
    }

    @Override
    public M delete(M model) {
        return deleteById(model.getId());
    }

    @Override
    public M update(M model) {
        E entity = modelMapper.map(model, getEntityClass());
        return modelMapper.map(genericDao.merge(entity).orElseThrow(() -> new IdNotFoundException(entity.getId())),
                getModelClass());
    }

    @Override
    protected Class<E> getInputClass() {
        return getEntityClass();
    }

    @Override
    protected Class<M> getOutputClass() {
        return getModelClass();
    }

    protected abstract Class<E> getEntityClass();

    protected abstract Class<M> getModelClass();

}
