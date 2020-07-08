package com.code.to.learn.persistence.dao.impl;

import com.code.to.learn.persistence.dao.api.RoleDao;
import com.code.to.learn.persistence.domain.entity.Role;
import com.code.to.learn.persistence.domain.entity.entity_enum.UserRole;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Optional;

@Repository
public class RoleDaoImpl extends GenericDaoImpl<Role> implements RoleDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoleDaoImpl.class);

    @Autowired
    public RoleDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @PostConstruct
    public void init() {
        try {
            if (count() == 0) {
                createRoles();
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private void createRoles() {
        for (UserRole userRole : UserRole.values()) {
            Role role = new Role();
            role.setName(userRole);
            persist(role);
        }
    }

    @Override
    public long count() {
        return executeInNewTransaction((session, transaction) -> {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
            Root<Role> root = criteriaQuery.from(getDomainClassType());
            criteriaQuery.select(criteriaBuilder.count(root));
            return getOrEmpty(session, criteriaQuery).get();
        });
    }

    @Override
    public void persist(Role role) {
        executeInNewTransaction((session, transaction) -> {
            session.persist(role);
            return null;
        });
    }

    @Override
    protected Class<Role> getDomainClassType() {
        return Role.class;
    }

    @Override
    public Optional<Role> findByName(UserRole name) {
        return findByField(Role.NAME, name);
    }
}
