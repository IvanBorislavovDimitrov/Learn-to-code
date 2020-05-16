package com.code.to.learn.persistence.service.impl;

import com.code.to.learn.persistence.constant.Messages;
import com.code.to.learn.persistence.dao.api.UserDao;
import com.code.to.learn.persistence.domain.entity.IdEntity;
import com.code.to.learn.persistence.domain.entity.User;
import com.code.to.learn.persistence.domain.model.UserChangePasswordServiceModel;
import com.code.to.learn.persistence.domain.model.UserForgottenPasswordServiceModel;
import com.code.to.learn.persistence.domain.model.UserServiceModel;
import com.code.to.learn.persistence.exception.basic.InvalidTokenException;
import com.code.to.learn.persistence.exception.basic.NotFoundException;
import com.code.to.learn.persistence.service.api.UserService;
import com.code.to.learn.persistence.util.ResetPasswordTokenGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

@Component
public class UserServiceImpl extends GenericServiceImpl<User, UserServiceModel> implements UserService {

    private final UserDao userDao;
    private final ResetPasswordTokenGenerator resetPasswordTokenGenerator;

    @Autowired
    public UserServiceImpl(UserDao userDao, ModelMapper modelMapper, ResetPasswordTokenGenerator resetPasswordTokenGenerator) {
        super(userDao, modelMapper);
        this.userDao = userDao;
        this.resetPasswordTokenGenerator = resetPasswordTokenGenerator;
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
    public List<UserServiceModel> findTeachers() {
        List<User> teachers = userDao.findTeachers();
        return toOutput(teachers);
    }

    @Override
    public UserServiceModel activateAccount(String username) {
        UserServiceModel userServiceModel = findByUsername(username);
        userServiceModel.setEnabled(true);
        update(userServiceModel);
        return userServiceModel;
    }

    @Override
    public List<UserServiceModel> findUsersByPage(int page, int maxResults) {
        List<User> users = userDao.findUsersByPage(page, maxResults);
        return toOutput(users);
    }

    @Override
    public UserForgottenPasswordServiceModel generateResetPasswordToken(String username) {
        String resetPasswordToken = resetPasswordTokenGenerator.generateResetPasswordToken();
        User user = getOrThrowNotFound(() -> userDao.findByUsername(username), Messages.USERNAME_NOT_FOUND, username);
        user.setResetPasswordToken(resetPasswordToken);
        userDao.update(user);
        return new UserForgottenPasswordServiceModel(resetPasswordToken, user.getEmail());
    }

    @Override
    public UserServiceModel changeForgottenPassword(UserChangePasswordServiceModel userChangePasswordServiceModel) {
        User user = super.getOrThrowNotFound(() -> userDao.findByResetPasswordToken(userChangePasswordServiceModel.getToken()),
                Messages.USERNAME_NOT_FOUND, userChangePasswordServiceModel.getConfirmPassword());
        if (!Objects.equals(user.getResetPasswordToken(), userChangePasswordServiceModel.getToken())) {
            throw new InvalidTokenException("Invalid token: {0}", user);
        }
        user.setPassword(userChangePasswordServiceModel.getPassword());
        user.setResetPasswordToken(null);
        return toOutput(userDao.update(user).get());
    }

    @Override
    public void storeUserLoginInformation(String username, LocalDate date, String additionalInformation) {
        User user = getOrThrowNotFound(() -> userDao.findByUsername(username), Messages.USERNAME_NOT_FOUND, username);
        User.LoginRecord loginRecord = new User.LoginRecord();
        loginRecord.setDate(date);
        loginRecord.setAdditionalInformation(additionalInformation);
        user.getLoginRecords().add(loginRecord);
        userDao.update(user);
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
