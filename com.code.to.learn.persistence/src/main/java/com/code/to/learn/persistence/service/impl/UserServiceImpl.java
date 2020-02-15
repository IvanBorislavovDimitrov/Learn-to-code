package com.code.to.learn.persistence.service.impl;

import com.code.to.learn.persistence.dao.api.RoleDao;
import com.code.to.learn.persistence.dao.api.UserDao;
import com.code.to.learn.persistence.domain.entity.Role;
import com.code.to.learn.persistence.domain.entity.User;
import com.code.to.learn.persistence.domain.model.UserServiceModel;
import com.code.to.learn.persistence.hibernate.HibernateUtils;
import com.code.to.learn.persistence.service.api.UserService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserServiceImpl extends GenericServiceImpl<User, UserServiceModel> implements UserService {

    private final UserDao userDao;
    private final RoleDao roleDao;

    @Autowired
    public UserServiceImpl(UserDao userDao, ModelMapper modelMapper, RoleDao roleDao) {
        super(userDao, modelMapper);
        this.userDao = userDao;
        this.roleDao = roleDao;
    }

    @Override
    public void registerUser(UserServiceModel userServiceModel) {
        SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            User user = modelMapper.map(userServiceModel, User.class);
            updateRolesForUser(user, session);
            userDao.persist(user, session);
        }
    }

    @Override
    public boolean isUsernameTaken(String username) {
        SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            return userDao.findByUsername(username, session).isPresent();
        }
    }

    @Override
    public boolean isEmailTaken(String email) {
        SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            return userDao.findByEmail(email, session).isPresent();
        }
    }

    @Override
    public boolean isPhoneNumberTaken(String phoneNumber) {
        SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            return userDao.findByPhoneNumber(phoneNumber, session).isPresent();
        }
    }

    @Override
    public Optional<UserServiceModel> findByUsername(String username) {
        SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            Optional<User> user = userDao.findByUsername(username, session);
            if (!user.isPresent()) {
                return Optional.empty();
            }
            UserServiceModel userServiceModel = modelMapper.map(user.get(), UserServiceModel.class);
            return Optional.of(userServiceModel);
        }
    }

    @Override
    public long findUsersCount() {
        SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            return userDao.findUsersCount(session);
        }
    }

    @Override
    protected Class<UserServiceModel> getModelClass() {
        return UserServiceModel.class;
    }

    @Override
    protected Class<User> getEntityClass() {
        return User.class;
    }

    private void updateRolesForUser(User user, Session session) {
        for (Role role : user.getRoles()) {
            Optional<Role> optionalRole = roleDao.findById(role.getId(), session);
            if (!optionalRole.isPresent()) {
                continue;
            }
            optionalRole.get().getUsers().add(user);
            roleDao.update(optionalRole.get(), session);
        }
    }

}
