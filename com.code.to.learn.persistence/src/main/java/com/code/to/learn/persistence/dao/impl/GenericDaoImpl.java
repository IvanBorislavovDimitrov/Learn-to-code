package com.code.to.learn.persistence.dao.impl;

import com.code.to.learn.persistence.dao.api.GenericDao;
import com.code.to.learn.persistence.domain.entity.IdEntity;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public abstract class GenericDaoImpl<E extends IdEntity> implements GenericDao<E> {

    @Override
    public List<E> findAll(Session session) {
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<E> criteriaQuery = criteriaBuilder.createQuery(getDomainClassType());
        Root<E> root = criteriaQuery.from(getDomainClassType());
        criteriaQuery.select(root);
        Query<E> query = session.createQuery(criteriaQuery);
        return query.getResultList();
    }

    @Override
    public Optional<E> findById(String id, Session session) {
        return findByField(IdEntity.ID, id, session);
    }

    @Override
    public void persist(E entity, Session session) {
        executeInTransaction(session, () -> session.persist(entity));
    }

    @Override
    public Optional<E> deleteById(String id, Session session) {
        Optional<E> entity = findById(id, session);
        if (entity.isPresent()) {
            delete(entity.get(), session);
            return entity;
        }
        return Optional.empty();
    }

    @Override
    public Optional<E> delete(E entity, Session session) {
        executeInTransaction(session, () -> session.delete(entity));
        return Optional.of(entity);
    }

    @Override
    public Optional<E> update(E entity, Session session) {
        executeInTransaction(session, () -> session.update(entity));
        return Optional.of(entity);
    }

    protected Optional<E> findByField(String field, Object value, Session session) {
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<E> criteriaQuery = criteriaBuilder.createQuery(getDomainClassType());
        Root<E> root = criteriaQuery.from(getDomainClassType());
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get(field), value));
        return (Optional<E>) getOrEmpty(session, criteriaQuery);
    }

    protected Optional<?> getOrEmpty(Session session, CriteriaQuery<?> criteriaQuery) {
        try {
            return getSingleResult(session, criteriaQuery);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    private Optional<?> getSingleResult(Session session, CriteriaQuery<?> criteriaQuery) {
        return getInTransaction(session, () -> Optional.of(session.createQuery(criteriaQuery).getSingleResult()));
    }

    private void executeInTransaction(Session session, Runnable runnable) {
        Transaction transaction = session.getTransaction();
        transaction.begin();
        runnable.run();
        transaction.commit();
    }

    private <T> T getInTransaction(Session session, Supplier<T> supplier) {
        Transaction transaction = session.getTransaction();
        transaction.begin();
        T result = supplier.get();
        transaction.commit();
        return result;
    }

    protected abstract Class<E> getDomainClassType();
}
