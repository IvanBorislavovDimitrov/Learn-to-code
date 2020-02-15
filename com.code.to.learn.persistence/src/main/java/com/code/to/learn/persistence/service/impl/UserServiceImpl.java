package com.code.to.learn.persistence.service.impl;

import com.code.to.learn.persistence.domain.entity.User;
import com.code.to.learn.persistence.domain.model.UserServiceModel;
import com.code.to.learn.persistence.repository.api.UserRepository;
import com.code.to.learn.persistence.service.api.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserServiceImpl extends GenericServiceImpl<User, UserServiceModel> implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        super(userRepository, modelMapper);
        this.userRepository = userRepository;
    }

    @Override
    public void registerUser(UserServiceModel userServiceModel) {
        User user = modelMapper.map(userServiceModel, User.class);
        userRepository.persist(user);
    }

    @Override
    public boolean isUsernameTaken(String username) {
        return userRepository.findUserByUsername(username).isPresent();
    }

    @Override
    public boolean isEmailTaken(String email) {
        return userRepository.findUserByEmail(email).isPresent();
    }

    @Override
    public boolean isPhoneNumberTaken(String phoneNumber) {
        return userRepository.findUserByPhoneNumber(phoneNumber).isPresent();
    }

    @Override
    public Optional<UserServiceModel> findByUsername(String username) {
        Optional<User> user = userRepository.findUserByUsername(username);
        if (!user.isPresent()) {
            return Optional.empty();
        }
        UserServiceModel userServiceModel = modelMapper.map(user.get(), UserServiceModel.class);
        return Optional.of(userServiceModel);
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
