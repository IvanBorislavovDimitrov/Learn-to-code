package com.code.to.learn.persistence.dao.api;

import com.code.to.learn.persistence.domain.entity.Role;
import org.hibernate.Session;

import java.util.Optional;

public interface RoleDao extends GenericDao<Role> {

    Optional<Role> findByName(String name, Session session);
}
