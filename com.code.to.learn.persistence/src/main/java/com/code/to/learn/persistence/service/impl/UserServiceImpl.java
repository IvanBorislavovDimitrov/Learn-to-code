package com.code.to.learn.persistence.service.impl;

import com.code.to.learn.persistence.constant.Messages;
import com.code.to.learn.persistence.dao.api.UserDao;
import com.code.to.learn.persistence.domain.entity.IdEntity;
import com.code.to.learn.persistence.domain.entity.User;
import com.code.to.learn.persistence.domain.model.UserServiceModel;
import com.code.to.learn.persistence.exception.basic.NotFoundException;
import com.code.to.learn.persistence.service.api.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Component
public class UserServiceImpl extends GenericServiceImpl<User, UserServiceModel> implements UserService {

    private final UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao, ModelMapper modelMapper) {
        super(userDao, modelMapper);
        this.userDao = userDao;
    }

    @Override
    public void registerUser(UserServiceModel userServiceModel) {
        User user = toInput(userServiceModel);
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
    public UserServiceModel findByUsername(String username) {
        User user = getOrThrowNotFound(() -> userDao.findByUsername(username), Messages.USERNAME_NOT_FOUND, username);
        return toOutput(user);
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
    protected <T extends IdEntity<T>> T getOrThrowNotFound(Supplier<Optional<T>> supplier, String exceptionMessage, Object... args) {
        try {
            return super.getOrThrowNotFound(supplier, exceptionMessage, args);
        } catch (NotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage());
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

}
