package com.code.to.learn.persistence.dao.impl;

import com.code.to.learn.persistence.dao.api.UserDao;
import com.code.to.learn.persistence.domain.entity.User;
import com.code.to.learn.persistence.util.DatabaseSessionUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Optional;

@Repository
public class UserDaoImpl extends GenericDaoImpl<User> implements UserDao {

    @Autowired
    public UserDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return findByField(User.USERNAME, username);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return findByField(User.EMAIL, email);
    }

    @Override
    public Optional<User> findByPhoneNumber(String phoneNumber) {
        return findByField(User.PHONE_NUMBER, phoneNumber);
    }

    @Override
    public long findUsersCount() {
        Session session = DatabaseSessionUtil.getCurrentOrOpen(sessionFactory);
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<User> root = criteriaQuery.from(getDomainClassType());
        criteriaQuery.select(criteriaBuilder.count(root));
        return ((Optional<Long>) getOrEmpty(session, criteriaQuery)).get();
    }

    @Override
    protected Class<User> getDomainClassType() {
        return User.class;
    }

}
