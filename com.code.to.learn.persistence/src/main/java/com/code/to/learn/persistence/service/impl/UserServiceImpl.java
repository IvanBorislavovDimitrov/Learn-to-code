package com.code.to.learn.persistence.service.impl;

import com.code.to.learn.persistence.domain.db.User;
import com.code.to.learn.persistence.domain.model.UserServiceModel;
import com.code.to.learn.persistence.exception.IdNotFoundException;
import com.code.to.learn.persistence.repository.api.UserRepository;
import com.code.to.learn.persistence.service.api.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<UserServiceModel> findAll() {
        return userRepository.getAll().stream()
                .map(user -> modelMapper.map(user, UserServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserServiceModel findById(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new IdNotFoundException(id));
        return modelMapper.map(user, UserServiceModel.class);
    }

    @Override
    public void save(UserServiceModel userServiceModel) {
        User user = modelMapper.map(userServiceModel, User.class);
        userRepository.persist(user);
    }

    @Override
    public UserServiceModel deleteById(String id) {
        User user = userRepository.deleteById(id).orElseThrow(() -> new IdNotFoundException(id));
        return modelMapper.map(user, UserServiceModel.class);
    }

    @Override
    public UserServiceModel delete(UserServiceModel userServiceModel) {
        return deleteById(userServiceModel.getId());
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

}
