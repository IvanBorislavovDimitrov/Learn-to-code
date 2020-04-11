package com.code.to.learn.persistence.service.api;

import com.code.to.learn.persistence.domain.model.RoleServiceModel;

public interface RoleService extends GenericService<RoleServiceModel> {

    RoleServiceModel findByName(String name);
}
