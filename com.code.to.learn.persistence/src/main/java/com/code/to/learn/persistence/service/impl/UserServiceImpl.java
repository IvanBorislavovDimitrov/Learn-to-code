package com.code.to.learn.persistence.service.impl;

import com.code.to.learn.persistence.domain.User;
import com.code.to.learn.persistence.domain.model.UserServiceModel;
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
    public List<UserServiceModel> getAll() {
        return userRepository.getAll().stream()
                .map(user -> modelMapper.map(user, UserServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserServiceModel getById(String id) {
        User user = userRepository.getById(id);
        return modelMapper.map(user, UserServiceModel.class);
    }

    @Override
    public void save(UserServiceModel userServiceModel) {
        User user = modelMapper.map(userServiceModel, User.class);
        userRepository.persist(user);
    }

    @Override
    public UserServiceModel deleteById(String id) {
        User user = userRepository.deleteById(id);
        return modelMapper.map(user, UserServiceModel.class);
    }

    @Override
    public UserServiceModel delete(UserServiceModel userServiceModel) {
        return deleteById(userServiceModel.getId());
    }
}
