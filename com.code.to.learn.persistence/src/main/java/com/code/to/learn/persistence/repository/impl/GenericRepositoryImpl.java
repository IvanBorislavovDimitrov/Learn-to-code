package com.code.to.learn.persistence.repository.impl;

import com.code.to.learn.persistence.domain.db.IdEntity;
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

// Refactor using more generic selection
public abstract class GenericRepositoryImpl<T extends IdEntity> implements GenericRepository<T> {

    @Override
    public List<T> getAll() {
        SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(getDomainClassType());
            Root<T> root = criteriaQuery.from(getDomainClassType());
            criteriaQuery.select(root);
            Query<T> query = session.createQuery(criteriaQuery);
            return query.getResultList();
        }
    }

    @Override
    public Optional<T> findById(String id) {
        return findByField(IdEntity.ID, id, false);
    }

    @Override
    public void persist(T obj) {
        SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            executeInTransaction(session, () -> session.persist(obj));
        }
    }

    @Override
    public Optional<T> deleteById(String id) {
        Optional<T> obj = findById(id);
        if (obj.isPresent()) {
            delete(obj.get());
            return obj;
        }
        return Optional.empty();
    }

    @Override
    public Optional<T> delete(T obj) {
        SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            executeInTransaction(session, () -> session.delete(obj));
        }
        return Optional.of(obj);
    }

    private void executeInTransaction(Session session, Runnable runnable) {
        session.getTransaction().begin();
        runnable.run();
        session.getTransaction().commit();
    }

    protected Optional<T> findByField(String field, String value) {
        return findByField(field, value, true);
    }

    protected Optional<T> findByField(String field, String value, boolean safe) {
        SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(getDomainClassType());
            Root<T> root = criteriaQuery.from(getDomainClassType());
            criteriaQuery.select(root).where(criteriaBuilder.equal(root.get(field), value));
            if (safe) {
                return getOrEmpty(session, criteriaQuery);
            }
            return getSingleResult(session, criteriaQuery);
        }
    }

    protected Optional<T> getOrEmpty(Session session, CriteriaQuery<T> criteriaQuery) {
        try {
            return getSingleResult(session, criteriaQuery);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    private Optional<T> getSingleResult(Session session, CriteriaQuery<T> criteriaQuery) {
        return Optional.of(session.createQuery(criteriaQuery).getSingleResult());
    }

    protected abstract Class<T> getDomainClassType();
}
