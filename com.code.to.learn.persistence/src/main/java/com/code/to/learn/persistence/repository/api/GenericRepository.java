package com.code.to.learn.persistence.repository.api;

import com.code.to.learn.persistence.domain.IdEntity;

import java.util.List;

public interface GenericRepository<T extends IdEntity> {

    List<T> getAll();

    T getById(String id);

    void persist(T obj);

    T deleteById(String id);

    T delete(T obj);
}
