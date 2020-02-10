package com.code.to.learn.persistence.repository.impl;

import com.code.to.learn.persistence.domain.entity.Role;
import com.code.to.learn.persistence.domain.entity.entity_enum.UserRole;
import com.code.to.learn.persistence.repository.api.RoleRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;

@Repository
public class RoleRepositoryImpl extends GenericRepositoryImpl<Role> implements RoleRepository {

    @PostConstruct
    public void init() {
        if (getAll().isEmpty()) {
            createRoles();
        }
    }

    private void createRoles() {
        for (UserRole userRole : UserRole.values()) {
            Role role = new Role();
            role.setUserRole(userRole);
            persist(role);
        }
    }

    @Override
    protected Class<Role> getDomainClassType() {
        return Role.class;
    }
}
