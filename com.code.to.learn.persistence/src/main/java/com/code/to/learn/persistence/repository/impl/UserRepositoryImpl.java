package com.code.to.learn.persistence.repository.impl;

import com.code.to.learn.persistence.constant.Constants;
import com.code.to.learn.persistence.domain.User;
import com.code.to.learn.persistence.hibernate.HibernateUtils;
import com.code.to.learn.persistence.repository.api.UserRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Component
public class UserRepositoryImpl extends GenericRepositoryImpl<User> implements UserRepository {

    @Override
    public User getUserByUsername(String username) {
        try (SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
             Session session = sessionFactory.openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(getDomainClassType());
            Root<User> root = criteriaQuery.from(getDomainClassType());
            criteriaQuery.select(root).where(criteriaBuilder.equal(root.get(Constants.USERNAME), username));
            return getOrNull(session, criteriaQuery);
        }
    }

    @Override
    protected Class<User> getDomainClassType() {
        return User.class;
    }

}
