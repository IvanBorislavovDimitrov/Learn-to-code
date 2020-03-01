package com.code.to.learn.persistence.service.api;

import java.util.Optional;

import com.code.to.learn.persistence.domain.model.RoleServiceModel;

public interface RoleService extends GenericService<RoleServiceModel> {

    Optional<RoleServiceModel> findByName(String name);
}
