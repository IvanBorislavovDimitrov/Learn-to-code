package com.code.to.learn.persistence.service.impl;

import com.code.to.learn.persistence.domain.entity.Role;
import com.code.to.learn.persistence.domain.model.RoleServiceModel;
import com.code.to.learn.persistence.repository.api.GenericRepository;
import com.code.to.learn.persistence.service.api.RoleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends GenericServiceImpl<Role, RoleServiceModel> implements RoleService {

    @Autowired
    public RoleServiceImpl(GenericRepository<Role> genericRepository, ModelMapper modelMapper) {
        super(genericRepository, modelMapper);
    }

    @Override
    protected Class<RoleServiceModel> getModelClass() {
        return RoleServiceModel.class;
    }

    @Override
    protected Class<Role> getEntityClass() {
        return Role.class;
    }
}
