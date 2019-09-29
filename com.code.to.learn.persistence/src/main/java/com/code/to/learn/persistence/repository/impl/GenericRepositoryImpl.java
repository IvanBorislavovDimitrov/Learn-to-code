package com.code.to.learn.persistence.repository.impl;

import com.code.to.learn.persistence.constant.Constants;
import com.code.to.learn.persistence.domain.IdEntity;
import com.code.to.learn.persistence.hibernate.HibernateUtils;
import com.code.to.learn.persistence.repository.api.GenericRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.function.Supplier;

// Refactor using more generic selection
public abstract class GenericRepositoryImpl<T extends IdEntity> implements GenericRepository<T> {

    @Override
    public List<T> getAll() {
        try (SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
             Session session = sessionFactory.openSession()) {
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
        try (SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
             Session session = sessionFactory.openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(getDomainClassType());
            Root<T> root = criteriaQuery.from(getDomainClassType());
            criteriaQuery.select(root).where(criteriaBuilder.equal(root.get(Constants.ID), id));
            Query<T> query = session.createQuery(criteriaQuery);
            return query.getSingleResult();
        }
    }

    @Override
    public void persist(T obj) {
        try (SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
             Session session = sessionFactory.openSession()) {
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
        try (SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
             Session session = sessionFactory.openSession()) {
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

    protected abstract Class<T> getDomainClassType();
}
