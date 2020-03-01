package com.code.to.learn.persistence.dao.api;

import com.code.to.learn.persistence.domain.entity.Role;
import com.code.to.learn.persistence.domain.entity.entity_enum.UserRole;

import java.util.Optional;

public interface RoleDao extends GenericDao<Role> {

    Optional<Role> findByName(UserRole name);
}
