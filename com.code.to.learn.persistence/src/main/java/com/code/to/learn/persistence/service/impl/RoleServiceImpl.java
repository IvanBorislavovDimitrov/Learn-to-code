package com.code.to.learn.persistence.service.impl;

import com.code.to.learn.persistence.constant.Messages;
import com.code.to.learn.persistence.dao.api.RoleDao;
import com.code.to.learn.persistence.domain.entity.Role;
import com.code.to.learn.persistence.domain.entity.entity_enum.UserRole;
import com.code.to.learn.persistence.domain.model.RoleServiceModel;
import com.code.to.learn.persistence.service.api.RoleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends GenericServiceImpl<Role, RoleServiceModel> implements RoleService {

    private final RoleDao roleDao;

    @Autowired
    public RoleServiceImpl(RoleDao roleDao, ModelMapper modelMapper) {
        super(roleDao, modelMapper);
        this.roleDao = roleDao;
    }

    @Override
    protected Class<RoleServiceModel> getModelClass() {
        return RoleServiceModel.class;
    }

    @Override
    protected Class<Role> getEntityClass() {
        return Role.class;
    }

    @Override
    public RoleServiceModel findByName(String name) {
        UserRole role = UserRole.valueOf(name);
        Role optionalRole = getOrThrowNotFound(() -> roleDao.findByName(role), Messages.ROLE_NOT_FOUND, name);
        return toOutput(optionalRole);
    }
}
