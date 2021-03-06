package com.code.to.learn.persistence.service.impl;

import com.code.to.learn.persistence.dao.api.GenericDao;
import com.code.to.learn.persistence.domain.entity.IdEntity;
import com.code.to.learn.persistence.domain.model.IdServiceModel;
import com.code.to.learn.persistence.exception.basic.IdNotFoundException;
import com.code.to.learn.persistence.exception.basic.NotFoundException;
import com.code.to.learn.persistence.service.api.GenericService;
import com.code.to.learn.util.mapper.ExtendableMapper;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public abstract class GenericServiceImpl<E extends IdEntity<E>, M extends IdServiceModel> extends ExtendableMapper<E, M> implements GenericService<M> {

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
        E updatedEntity = getUpdatedEntity(model);
        return toOutput(genericDao.update(updatedEntity).get());
    }

    @Override
    public M merge(M model) {
        E updatedEntity = getUpdatedEntity(model);
        return toOutput(genericDao.merge(updatedEntity).get());
    }

    private E getUpdatedEntity(M model) {
        Optional<E> entity = genericDao.findById(model.getId());
        if (!entity.isPresent()) {
            throw new IdNotFoundException(model.getId());
        }
        E mappedEntity = toInput(model);
        return entity.get().merge(mappedEntity);
    }

    @Override
    public long count() {
        return genericDao.count();
    }

    @Override
    protected Class<E> getInputClass() {
        return getEntityClass();
    }

    protected <T extends IdEntity<T>> T getOrThrowNotFound(Supplier<Optional<T>> supplier, String exceptionMessage, Object... args) {
        Optional<T> optionalIdServiceModel = supplier.get();
        if (!optionalIdServiceModel.isPresent()) {
            throw new NotFoundException(exceptionMessage, args);
        }
        return optionalIdServiceModel.get();
    }

    public <T extends IdEntity<T>> T getWithoutCheck(Supplier<Optional<T>> supplier) {
        return supplier.get().get();
    }

    @Override
    protected Class<M> getOutputClass() {
        return getModelClass();
    }

    protected GenericDao<E> getGenericDao() {
        return genericDao;
    }

    protected abstract Class<E> getEntityClass();

    protected abstract Class<M> getModelClass();

}
