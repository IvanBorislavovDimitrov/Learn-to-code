package com.code.to.learn.persistence.dao.impl;

import com.code.to.learn.persistence.dao.api.RoleDao;
import com.code.to.learn.persistence.domain.entity.Role;
import com.code.to.learn.persistence.domain.entity.entity_enum.UserRole;
import com.code.to.learn.persistence.util.DatabaseSessionUtil;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Repository
public class RoleDaoImpl extends GenericDaoImpl<Role> implements RoleDao {

    @Autowired
    public RoleDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @PostConstruct
    public void init() {
        if (count() == 0) {
            createRoles();
        }
        DatabaseSessionUtil.closeSessionWithCommit(getSessionFactory());
    }

    private void createRoles() {
        for (UserRole userRole : UserRole.values()) {
            Role role = new Role();
            role.setName(userRole);
            persist(role);
        }
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
