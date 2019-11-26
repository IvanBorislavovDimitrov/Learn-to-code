package com.code.to.learn.persistence.service.api;

import java.util.List;

public interface Service<T> {

    List<T> findAll();

    T findById(String id);

    void save(T obj);

    T deleteById(String id);

    T delete(T obj);
}
