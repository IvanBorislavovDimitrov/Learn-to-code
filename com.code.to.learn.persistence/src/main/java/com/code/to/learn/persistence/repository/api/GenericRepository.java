package com.code.to.learn.persistence.repository.api;

import com.code.to.learn.persistence.domain.db.IdEntity;

import java.util.List;
import java.util.Optional;

public interface GenericRepository<T extends IdEntity> {

    List<T> getAll();

    Optional<T> findById(String id);

    void persist(T obj);

    Optional<T> deleteById(String id);

    Optional<T> delete(T obj);
}
