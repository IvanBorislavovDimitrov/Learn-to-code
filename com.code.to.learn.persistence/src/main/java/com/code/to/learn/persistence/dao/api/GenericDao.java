package com.code.to.learn.persistence.dao.api;

import com.code.to.learn.persistence.domain.entity.IdEntity;

import java.util.List;
import java.util.Optional;

public interface GenericDao<E extends IdEntity> {

    List<E> findAll();

    Optional<E> findById(String id);

    void persist(E entity);

    Optional<E> deleteById(String id);

    Optional<E> delete(E entity);

    Optional<E> update(E entity);

    long count();
}
