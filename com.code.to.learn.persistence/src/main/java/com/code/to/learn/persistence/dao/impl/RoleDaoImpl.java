package com.code.to.learn.persistence.dao.impl;

import com.code.to.learn.persistence.dao.api.RoleDao;
import com.code.to.learn.persistence.domain.entity.Role;
import com.code.to.learn.persistence.domain.entity.entity_enum.UserRole;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class RoleDaoImpl extends GenericDaoImpl<Role> implements RoleDao {

    @Override
    protected Class<Role> getDomainClassType() {
        return Role.class;
    }

    @Override
    public Optional<Role> findByName(UserRole name, Session session) {
        return findByField(Role.NAME, name, session);
    }
}
