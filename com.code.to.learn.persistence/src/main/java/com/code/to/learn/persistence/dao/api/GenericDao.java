package com.code.to.learn.persistence.dao.api;

import com.code.to.learn.persistence.domain.entity.IdEntity;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

public interface GenericDao<E extends IdEntity> {

    List<E> findAll(Session session);

    Optional<E> findById(String id, Session session);

    void persist(E entity, Session session);

    Optional<E> deleteById(String id, Session session);

    Optional<E> delete(E entity, Session session);

    Optional<E> update(E entity, Session session);
}
