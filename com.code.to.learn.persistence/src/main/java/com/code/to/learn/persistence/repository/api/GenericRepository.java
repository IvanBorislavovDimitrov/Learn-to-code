package com.code.to.learn.persistence.repository.api;

import com.code.to.learn.persistence.domain.entity.IdEntity;

import java.util.List;
import java.util.Optional;

public interface GenericRepository<E extends IdEntity> {

    List<E> getAll();

    Optional<E> findById(String id);

    void persist(E entity);

    Optional<E> deleteById(String id);

    Optional<E> delete(E entity);

    Optional<E> update(E entity);
}
