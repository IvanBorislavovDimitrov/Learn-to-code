package com.code.to.learn.persistence.dao.api;

import com.code.to.learn.persistence.domain.entity.IdEntity;

import java.util.List;
import java.util.Optional;

public interface GenericDao<E extends IdEntity<E>> {

    List<E> findAll();

    Optional<E> findById(String id);

    void persist(E entity);

    Optional<E> deleteById(String id);

    Optional<E> delete(E entity);

    Optional<E> update(E entity);

    Optional<E> merge(E entity);

    Optional<E> findByField(String fieldName, Object value);

    long count();
}
