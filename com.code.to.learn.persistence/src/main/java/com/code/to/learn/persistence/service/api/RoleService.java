package com.code.to.learn.persistence.service.api;

import com.code.to.learn.persistence.domain.model.RoleServiceModel;

import java.util.Optional;

public interface RoleService extends GenericService<RoleServiceModel> {

    Optional<RoleServiceModel> findByName(String name);
}
