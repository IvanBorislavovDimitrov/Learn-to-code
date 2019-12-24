package com.code.to.learn.persistence.service.api;

import com.code.to.learn.persistence.domain.model.IdServiceModel;

import java.util.List;

public interface Service<M extends IdServiceModel> {

    List<M> findAll();

    M findById(String id);

    void save(M model);

    M deleteById(String model);

    M delete(M model);

    M update(M model);
}
