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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
        User user = toInput(userServiceModel);
        updateRolesForNewUser(user);
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
        UserServiceModel userServiceModel = toOutput(user.get());
        return Optional.of(userServiceModel);
    }

    @Override
    public long findUsersCount() {
        return userDao.count();
    }

    @Override
    public List<UserServiceModel> findUsersByUsernameContaining(String username) {
        List<User> users = userDao.findUsersByUsernameContaining(username);
        return toOutput(users);
    }

    @Override
    public UserServiceModel update(UserServiceModel userServiceModel) {
        User user = toInput(userServiceModel);
        removeUserFromAllRoles(user);
        addUserToRequiredRoles(user);
        UserServiceModel updatedModel = toOutput(user);
        return super.update(updatedModel);
    }

    private void removeUserFromAllRoles(User user) {
        for (Role role : roleDao.findAll()) {
            role.getUsers()
                    .removeIf(currentUser -> Objects.equals(currentUser.getUsername(), user.getUsername()));
        }
    }

    private void addUserToRequiredRoles(User user) {
        for (Role role : user.getRoles()) {
            Optional<Role> optionalRole = roleDao.findById(role.getId());
            optionalRole.get().getUsers().add(user);
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

    private void updateRolesForNewUser(User user) {
        List<Role> roles = new ArrayList<>();
        for (Role role : user.getRoles()) {
            Optional<Role> optionalRole = roleDao.findById(role.getId());
            if (!optionalRole.isPresent()) {
                continue;
            }
            roles.add(optionalRole.get());
        }
        user.setRoles(roles);
        roles.forEach(role -> role.getUsers().add(user));
    }

}
