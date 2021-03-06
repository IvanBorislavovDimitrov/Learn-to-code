package com.code.to.learn.persistence.dao.impl;

import com.code.to.learn.persistence.dao.api.GenericDao;
import com.code.to.learn.persistence.domain.entity.IdEntity;
import com.code.to.learn.persistence.exception.basic.LCException;
import com.code.to.learn.persistence.util.DatabaseSessionUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

public abstract class GenericDaoImpl<E extends IdEntity<E>> implements GenericDao<E> {

    protected final SessionFactory sessionFactory;

    public GenericDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<E> findAll() {
        Session session = DatabaseSessionUtil.getCurrentSession(sessionFactory);
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<E> criteriaQuery = criteriaBuilder.createQuery(getDomainClassType());
        Root<E> root = criteriaQuery.from(getDomainClassType());
        criteriaQuery.select(root);
        Query<E> query = session.createQuery(criteriaQuery);
        return query.getResultList();
    }

    @Override
    public Optional<E> findById(String id) {
        return findByField(IdEntity.ID, id);
    }

    @Override
    public void persist(E entity) {
        DatabaseSessionUtil.getCurrentSession(sessionFactory)
                .persist(entity);
    }

    @Override
    public Optional<E> deleteById(String id) {
        Optional<E> entity = findById(id);
        if (entity.isPresent()) {
            delete(entity.get());
            return entity;
        }
        return Optional.empty();
    }

    @Override
    public Optional<E> delete(E entity) {
        DatabaseSessionUtil.getCurrentSession(sessionFactory).delete(entity);
        return Optional.of(entity);
    }

    @Override
    public Optional<E> update(E entity) {
        DatabaseSessionUtil.getCurrentSession(sessionFactory).update(entity);
        return Optional.of(entity);
    }

    @Override
    public Optional<E> merge(E entity) {
        DatabaseSessionUtil.getCurrentSession(sessionFactory).merge(entity);
        return Optional.of(entity);
    }

    @Override
    public long count() {
        Session session = DatabaseSessionUtil.getCurrentSession(sessionFactory);
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<E> root = criteriaQuery.from(getDomainClassType());
        criteriaQuery.select(criteriaBuilder.count(root));
        return getOrEmpty(session, criteriaQuery).get();
    }

    @Override
    public Optional<E> findByField(String field, Object value) {
        Session session = DatabaseSessionUtil.getCurrentSession(sessionFactory);
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<E> criteriaQuery = criteriaBuilder.createQuery(getDomainClassType());
        Root<E> root = criteriaQuery.from(getDomainClassType());
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get(field), value));
        return getOrEmpty(session, criteriaQuery);
    }

    protected <T> Optional<T> getOrEmpty(Session session, CriteriaQuery<T> criteriaQuery) {
        try {
            return getSingleResult(session, criteriaQuery);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    protected String buildContainsExpression(String value) {
        return "%" + value + "%";
    }

    private <T> Optional<T> getSingleResult(Session session, CriteriaQuery<T> criteriaQuery) {
        return Optional.of(session.createQuery(criteriaQuery).getSingleResult());
    }

    protected <T> T executeInNewTransaction(BiFunction<Session, Transaction, T> supplier) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = DatabaseSessionUtil.openNewSession(sessionFactory);
            transaction = DatabaseSessionUtil.beginTransaction(session);
            T result = supplier.apply(session, transaction);
            DatabaseSessionUtil.commitTransaction(transaction);
            return result;
        } catch (Exception e) {
            DatabaseSessionUtil.rollbackTransaction(transaction);
            throw new LCException(e.getMessage(), e);
        } finally {
            DatabaseSessionUtil.closeSession(session);
        }

    }

    protected abstract Class<E> getDomainClassType();

    protected SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
