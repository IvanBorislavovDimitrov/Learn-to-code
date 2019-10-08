package com.code.to.learn.persistence.repository.impl;

import com.code.to.learn.persistence.constant.Constants;
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
import java.util.function.Supplier;

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
    public T getById(String id) {
        return getByField(IdEntity.ID, id, false);
    }

    @Override
    public void persist(T obj) {
        SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            executeInTransaction(session, () -> {
                session.persist(obj);
                return null;
            });
        }
    }

    @Override
    public T deleteById(String id) {
        T obj = getById(id);
        return delete(obj);
    }

    @Override
    public T delete(T obj) {
        SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            executeInTransaction(session, () -> {
                session.delete(obj);
                return null;
            });
        }
        return obj;
    }

    private void executeInTransaction(Session session, Supplier<T> supplier) {
        session.getTransaction().begin();
        supplier.get();
        session.getTransaction().commit();
    }

    protected T getByField(String field, String value) {
        return getByField(field, value, true);
    }

    protected T getByField(String field, String value, boolean safe) {
        SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(getDomainClassType());
            Root<T> root = criteriaQuery.from(getDomainClassType());
            criteriaQuery.select(root).where(criteriaBuilder.equal(root.get(field), value));
            if (safe) {
                return getOrNull(session, criteriaQuery);
            }
            return getSingleResult(session, criteriaQuery);
        }
    }

    protected T getOrNull(Session session, CriteriaQuery<T> criteriaQuery) {
        try {
            return getSingleResult(session, criteriaQuery);
        } catch (NoResultException e) {
            return null;
        }
    }

    private T getSingleResult(Session session, CriteriaQuery<T> criteriaQuery) {
        return session.createQuery(criteriaQuery).getSingleResult();
    }

    protected abstract Class<T> getDomainClassType();
}
