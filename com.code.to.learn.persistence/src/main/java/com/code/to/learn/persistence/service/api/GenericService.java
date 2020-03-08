package com.code.to.learn.persistence.service.api;

import java.util.List;

import com.code.to.learn.persistence.domain.model.GenericServiceModel;

public interface GenericService<M extends GenericServiceModel> {

    List<M> findAll();

    M findById(String id);

    void save(M model);

    M deleteById(String model);

    M delete(M model);

    M update(M model);
}
