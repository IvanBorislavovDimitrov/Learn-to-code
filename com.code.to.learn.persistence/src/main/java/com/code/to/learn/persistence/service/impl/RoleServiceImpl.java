package com.code.to.learn.persistence.service.impl;

import com.code.to.learn.persistence.dao.api.RoleDao;
import com.code.to.learn.persistence.domain.entity.Role;
import com.code.to.learn.persistence.domain.entity.entity_enum.UserRole;
import com.code.to.learn.persistence.domain.model.RoleServiceModel;
import com.code.to.learn.persistence.hibernate.HibernateUtils;
import com.code.to.learn.persistence.service.api.RoleService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Service
public class RoleServiceImpl extends GenericServiceImpl<Role, RoleServiceModel> implements RoleService {

    private final RoleDao roleDao;

    @Autowired
    public RoleServiceImpl(RoleDao roleDao, ModelMapper modelMapper) {
        super(roleDao, modelMapper);
        this.roleDao = roleDao;
    }

    @PostConstruct
    public void init() {
        if (findAll().isEmpty()) {
            createRoles();
        }
    }

    private void createRoles() {
        SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            for (UserRole userRole : UserRole.values()) {
                Role role = new Role();
                role.setName(userRole);
                roleDao.persist(role, session);
            }
        }

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
    public Optional<RoleServiceModel> findByName(String name) {
        SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            Optional<Role> optionalRole = roleDao.findByName(name, session);
            if (!optionalRole.isPresent()) {
                return Optional.empty();
            }
            RoleServiceModel roleServiceModel = modelMapper.map(optionalRole.get(), RoleServiceModel.class);
            return Optional.of(roleServiceModel);
        }
    }
}
