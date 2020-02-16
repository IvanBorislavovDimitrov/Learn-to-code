package com.code.to.learn.web.api_impl;

import com.code.to.learn.api.api.user.UserServiceApi;
import com.code.to.learn.api.model.user.UserBindingModel;
import com.code.to.learn.api.model.user.UserResponseModel;
import com.code.to.learn.core.constant.Messages;
import com.code.to.learn.core.exception.basic.NotFoundException;
import com.code.to.learn.core.validator.UserValidator;
import com.code.to.learn.persistence.domain.entity.entity_enum.UserRole;
import com.code.to.learn.persistence.domain.model.RoleServiceModel;
import com.code.to.learn.persistence.domain.model.UserServiceModel;
import com.code.to.learn.persistence.service.api.RoleService;
import com.code.to.learn.persistence.service.api.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("userServiceApiImpl")
public class UserServiceApiImpl implements UserServiceApi {

    private final ModelMapper modelMapper;
    private final UserValidator userValidator;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    @Autowired
    public UserServiceApiImpl(ModelMapper modelMapper, UserValidator userValidator, UserService userService, PasswordEncoder passwordEncoder, RoleService roleService) {
        this.modelMapper = modelMapper;
        this.userValidator = userValidator;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    @Override
    public ResponseEntity<List<UserResponseModel>> findAllUsers() {
        List<UserResponseModel> userViewModels = findUsers();
        return ResponseEntity.ok().body(userViewModels);
    }

    @Override
    public ResponseEntity<UserResponseModel> findUser(String username) {
        UserResponseModel userResponseModel = findByUsername(username);
        return ResponseEntity.ok(userResponseModel);
    }

    @Override
    public ResponseEntity<UserResponseModel> register(UserBindingModel userBindingModel) {
        userValidator.validateUserBindingModel(userBindingModel);
        UserServiceModel userServiceModel = toUserServiceModelWithEncodedPassword(userBindingModel);
        addRolesForUser(userServiceModel);
        convertAndSetDateToUser(userBindingModel, userServiceModel);
        userService.registerUser(userServiceModel);
        UserResponseModel userResponseModel = modelMapper.map(userServiceModel, UserResponseModel.class);
        return ResponseEntity.ok(userResponseModel);
    }

    public List<UserResponseModel> findUsers() {
        return userService.findAll().stream()
                .map(userServiceModel -> modelMapper.map(userServiceModel, UserResponseModel.class))
                .collect(Collectors.toList());
    }

    public UserResponseModel findByUsername(String username) {
        Optional<UserServiceModel> optionalUserServiceModel = userService.findByUsername(username);
        if (!optionalUserServiceModel.isPresent()) {
            throw new NotFoundException(Messages.USER_NOT_FOUND, username);
        }
        return modelMapper.map(optionalUserServiceModel.get(), UserResponseModel.class);
    }

    private UserServiceModel toUserServiceModelWithEncodedPassword(UserBindingModel userBindingModel) {
        UserBindingModel encodedPasswordUserBindingModel = getUserBindingModelWithEncodedPassword(userBindingModel);
        return modelMapper.map(encodedPasswordUserBindingModel, UserServiceModel.class);
    }

    private UserBindingModel getUserBindingModelWithEncodedPassword(UserBindingModel userBindingModel) {
        UserBindingModel encodedPasswordUserBindingModel = new UserBindingModel(userBindingModel);
        String planePassword = userBindingModel.getPassword();
        String encodedPassword = passwordEncoder.encode(planePassword);
        encodedPasswordUserBindingModel.setPassword(encodedPassword);
        encodedPasswordUserBindingModel.setConfirmPassword(encodedPassword);
        return encodedPasswordUserBindingModel;
    }

    private void addRolesForUser(UserServiceModel userServiceModel) {
        long usersCount = userService.findUsersCount();
        if (usersCount == 0) {
            List<RoleServiceModel> roles = roleService.findAll();
            roles.forEach(role -> role.getUsers().add(userServiceModel));
            userServiceModel.setRoles(roles);
        } else {
            RoleServiceModel roleServiceModel = roleService.findByName(UserRole.USER.toString()).get();
            roleServiceModel.getUsers().add(userServiceModel);
            userServiceModel.setRoles(Collections.singletonList(roleServiceModel));
        }
    }

    private void convertAndSetDateToUser(UserBindingModel userBindingModel, UserServiceModel userServiceModel) {
        LocalDate birthDate = LocalDate.parse(userBindingModel.getBirthDate());
        userServiceModel.setBirthDate(birthDate);
    }

}
