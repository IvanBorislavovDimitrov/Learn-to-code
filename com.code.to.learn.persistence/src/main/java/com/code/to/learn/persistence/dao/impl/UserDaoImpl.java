package com.code.to.learn.persistence.dao.impl;

import com.code.to.learn.persistence.dao.api.RoleDao;
import com.code.to.learn.persistence.dao.api.UserDao;
import com.code.to.learn.persistence.domain.entity.User;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Optional;

@Repository
public class UserDaoImpl extends GenericDaoImpl<User> implements UserDao {

    private final RoleDao roleRepository;

    @Autowired
    public UserDaoImpl(RoleDao roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Optional<User> findByUsername(String username, Session session) {
        return findByField(User.USERNAME, username, session);
    }

    @Override
    public Optional<User> findByEmail(String email, Session session) {
        return findByField(User.EMAIL, email, session);
    }

    @Override
    public Optional<User> findByPhoneNumber(String phoneNumber, Session session) {
        return findByField(User.PHONE_NUMBER, phoneNumber, session);
    }

    @Override
    public long findUsersCount(Session session) {
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
