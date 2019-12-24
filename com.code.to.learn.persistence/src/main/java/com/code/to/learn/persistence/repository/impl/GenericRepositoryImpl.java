package com.code.to.learn.persistence.repository.impl;

import com.code.to.learn.persistence.domain.entity.IdEntity;
import com.code.to.learn.persistence.hibernate.HibernateUtils;
import com.code.to.learn.persistence.repository.api.GenericRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

// Refactor using more generic selection
public abstract class GenericRepositoryImpl<E extends IdEntity> implements GenericRepository<E> {

    @Override
    public List<E> getAll() {
        SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<E> criteriaQuery = criteriaBuilder.createQuery(getDomainClassType());
            Root<E> root = criteriaQuery.from(getDomainClassType());
            criteriaQuery.select(root);
            Query<E> query = session.createQuery(criteriaQuery);
            return query.getResultList();
        }
    }

    @Override
    public Optional<E> findById(String id) {
        return findByField(IdEntity.ID, id, false);
    }

    @Override
    public void persist(E entity) {
        SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            executeInTransaction(session, () -> session.persist(entity));
        }
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
        SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            executeInTransaction(session, () -> session.delete(entity));
        }
        return Optional.of(entity);
    }

    @Override
    public Optional<E> update(E entity) {
        SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            executeInTransaction(session, () -> session.update(entity));
        }
        return Optional.of(entity);
    }

    protected Optional<E> findByField(String field, String value) {
        return findByField(field, value, true);
    }

    protected Optional<E> findByField(String field, String value, boolean safe) {
        SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<E> criteriaQuery = criteriaBuilder.createQuery(getDomainClassType());
            Root<E> root = criteriaQuery.from(getDomainClassType());
            criteriaQuery.select(root).where(criteriaBuilder.equal(root.get(field), value));
            if (safe) {
                return getOrEmpty(session, criteriaQuery);
            }
            return getSingleResult(session, criteriaQuery);
        }
    }

    protected Optional<E> getOrEmpty(Session session, CriteriaQuery<E> criteriaQuery) {
        try {
            return getSingleResult(session, criteriaQuery);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    private Optional<E> getSingleResult(Session session, CriteriaQuery<E> criteriaQuery) {
        return getInTransaction(session, () -> Optional.of(session.createQuery(criteriaQuery).getSingleResult()));
    }

    private void executeInTransaction(Session session, Runnable runnable) {
        session.getTransaction().begin();
        runnable.run();
        session.getTransaction().commit();
    }

    private <T> T getInTransaction(Session session, Supplier<T> supplier) {
        session.getTransaction().begin();
        T result = supplier.get();
        session.getTransaction().commit();
        return result;
    }

    protected abstract Class<E> getDomainClassType();
}
