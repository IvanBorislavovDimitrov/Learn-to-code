package com.code.to.learn.persistence.service.impl;

import com.code.to.learn.persistence.domain.entity.IdEntity;
import com.code.to.learn.persistence.domain.model.IdServiceModel;
import com.code.to.learn.persistence.exception.IdNotFoundException;
import com.code.to.learn.persistence.repository.api.GenericRepository;
import com.code.to.learn.persistence.service.api.GenericService;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public abstract class GenericServiceImpl<E extends IdEntity, M extends IdServiceModel> implements GenericService<M> {

    protected final ModelMapper modelMapper;
    private final GenericRepository<E> genericRepository;

    protected GenericServiceImpl(GenericRepository<E> genericRepository, ModelMapper modelMapper) {
        this.genericRepository = genericRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<M> findAll() {
        return genericRepository.getAll().stream()
                .map(idServiceEntity -> modelMapper.map(idServiceEntity, getModelClass()))
                .collect(Collectors.toList());
    }

    @Override
    public M findById(String id) {
        E entity = genericRepository.findById(id).orElseThrow(() -> new IdNotFoundException(id));
        return modelMapper.map(entity, getModelClass());
    }

    @Override
    public void save(M model) {
        E entity = modelMapper.map(model, getEntityClass());
        genericRepository.persist(entity);
    }

    @Override
    public M deleteById(String id) {
        E entity = genericRepository.deleteById(id).orElseThrow(() -> new IdNotFoundException(id));
        return modelMapper.map(entity, getModelClass());
    }

    @Override
    public M delete(M model) {
        return deleteById(model.getId());
    }

    @Override
    public M update(M model) {
        E entity = modelMapper.map(model, getEntityClass());
        return modelMapper.map(genericRepository.update(entity).orElseThrow(() -> new IdNotFoundException(entity.getId())),
                getModelClass());
    }

    protected abstract Class<M> getModelClass();

    protected abstract Class<E> getEntityClass();
}
