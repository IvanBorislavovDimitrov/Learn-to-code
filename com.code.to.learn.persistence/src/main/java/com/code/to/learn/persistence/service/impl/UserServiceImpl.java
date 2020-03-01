package com.code.to.learn.persistence.service.impl;

import com.code.to.learn.persistence.dao.api.RoleDao;
import com.code.to.learn.persistence.dao.api.UserDao;
import com.code.to.learn.persistence.domain.entity.Role;
import com.code.to.learn.persistence.domain.entity.User;
import com.code.to.learn.persistence.domain.model.UserServiceModel;
import com.code.to.learn.persistence.service.api.UserService;
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
        User user = modelMapper.map(userServiceModel, User.class);
        updateRolesForUser(user);
        userDao.persist(user);
    }

    @Override
    public boolean isUsernameTaken(String username) {
        return userDao.findByUsername(username).isPresent();
    }

    @Override
    public boolean isEmailTaken(String email) {
        return userDao.findByEmail(email).isPresent();
    }

    @Override
    public boolean isPhoneNumberTaken(String phoneNumber) {
        return userDao.findByPhoneNumber(phoneNumber).isPresent();
    }

    @Override
    public Optional<UserServiceModel> findByUsername(String username) {
        Optional<User> user = userDao.findByUsername(username);
        if (!user.isPresent()) {
            return Optional.empty();
        }
        UserServiceModel userServiceModel = modelMapper.map(user.get(), UserServiceModel.class);
        return Optional.of(userServiceModel);
    }

    @Override
    public long findUsersCount() {
        return userDao.findUsersCount();
    }

    @Override
    protected Class<UserServiceModel> getModelClass() {
        return UserServiceModel.class;
    }

    @Override
    protected Class<User> getEntityClass() {
        return User.class;
    }

    private void updateRolesForUser(User user) {
        for (Role role : user.getRoles()) {
            Optional<Role> optionalRole = roleDao.findById(role.getId());
            if (!optionalRole.isPresent()) {
                continue;
            }
            optionalRole.get().getUsers().add(user);
            roleDao.update(optionalRole.get());
        }
    }

}
