package com.code.to.learn.web.api_impl;

import com.code.to.learn.api.api.user.UserServiceApi;
import com.code.to.learn.api.model.user.UserBindingModel;
import com.code.to.learn.api.model.user.UserResponseModel;
import com.code.to.learn.core.validator.UserValidator;
import com.code.to.learn.persistence.constant.Messages;
import com.code.to.learn.persistence.domain.entity.entity_enum.UserRole;
import com.code.to.learn.persistence.domain.model.RoleServiceModel;
import com.code.to.learn.persistence.domain.model.UserServiceModel;
import com.code.to.learn.persistence.exception.basic.NotFoundException;
import com.code.to.learn.persistence.service.api.RoleService;
import com.code.to.learn.persistence.service.api.UserService;
import com.code.to.learn.util.mapper.ExtendableMapper;
import com.code.to.learn.web.util.FileToUpload;
import com.code.to.learn.web.util.MultipartFileUploader;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.code.to.learn.api.constant.Constants.DATE_PATTERN;
import static com.code.to.learn.web.constants.Constants.PROFILE_PICTURE_EXTENSION;

@Service
public class UserServiceApiImpl extends ExtendableMapper<UserServiceModel, UserResponseModel> implements UserServiceApi {

    private final UserValidator userValidator;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final MultipartFileUploader multipartFileUploader;

    @Autowired
    public UserServiceApiImpl(ModelMapper modelMapper, UserValidator userValidator, UserService userService,
                              PasswordEncoder passwordEncoder, RoleService roleService, MultipartFileUploader multipartFileUploader) {
        super(modelMapper);
        this.userValidator = userValidator;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.multipartFileUploader = multipartFileUploader;
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
        UserServiceModel userServiceModel = toUserServiceModel(userBindingModel);
        multipartFileUploader.uploadFileAsync(new FileToUpload(userServiceModel.getProfilePictureName(), userBindingModel.getProfilePicture()));
        addRolesForUser(userServiceModel);
        convertAndSetDateToUser(userBindingModel, userServiceModel);
        userService.registerUser(userServiceModel);
        UserResponseModel userResponseModel = toOutput(userServiceModel);
        return ResponseEntity.ok(userResponseModel);
    }


    @Override
    public ResponseEntity<List<UserResponseModel>> findUsersByUsernameContaining(String username) {
        List<UserServiceModel> userServiceModels = userService.findUsersByUsernameContaining(username);
        return ResponseEntity.ok(toOutput(userServiceModels));
    }

    @Override
    public ResponseEntity<UserResponseModel> changeUserRoles(String username, List<String> roles) {
        UserServiceModel userServiceModel = getByUsername(username);
        userServiceModel.setRoles(toRoleServiceModels(roles));
        UserServiceModel updatedUserServiceModel = userService.update(userServiceModel);
        return ResponseEntity.ok(toOutput(updatedUserServiceModel));
    }

    private List<UserResponseModel> findUsers() {
        return userService.findAll().stream()
                .map(this::toOutput)
                .collect(Collectors.toList());
    }

    private UserResponseModel findByUsername(String username) {
        UserServiceModel userServiceModel = getByUsername(username);
        return toOutput(userServiceModel);
    }

    private UserServiceModel getByUsername(String username) {
        Optional<UserServiceModel> optionalUserServiceModel = userService.findByUsername(username);
        if (!optionalUserServiceModel.isPresent()) {
            throw new NotFoundException(Messages.USER_NOT_FOUND, username);
        }
        return optionalUserServiceModel.get();
    }

    private List<RoleServiceModel> toRoleServiceModels(List<String> roles) {
        List<RoleServiceModel> roleServiceModels = new ArrayList<>();
        for (String role : roles) {
            Optional<RoleServiceModel> optionalRoleServiceModel = roleService.findByName(role);
            if (!optionalRoleServiceModel.isPresent()) {
                continue;
            }
            roleServiceModels.add(optionalRoleServiceModel.get());
        }
        return roleServiceModels;
    }

    private UserServiceModel toUserServiceModel(UserBindingModel userBindingModel) {
        UserBindingModel encodedPasswordUserBindingModel = getUserBindingModelWithEncodedPassword(userBindingModel);
        UserServiceModel userServiceModel = getMapper().map(encodedPasswordUserBindingModel, UserServiceModel.class);
        String extension = FilenameUtils.getExtension(userBindingModel.getProfilePicture().getOriginalFilename());
        userServiceModel.setProfilePictureName(userServiceModel.getUsername() + PROFILE_PICTURE_EXTENSION + "." + extension);
        return userServiceModel;
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
            userServiceModel.setRoles(roles);
        } else {
            RoleServiceModel roleServiceModel = roleService.findByName(UserRole.ROLE_USER.toString()).get();
            userServiceModel.setRoles(Collections.singletonList(roleServiceModel));
        }
    }

    private void convertAndSetDateToUser(UserBindingModel userBindingModel, UserServiceModel userServiceModel) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
        LocalDate birthDate = LocalDate.parse(userBindingModel.getBirthDate(), formatter);
        userServiceModel.setBirthDate(birthDate);
    }

    @Override
    protected Class<UserServiceModel> getInputClass() {
        return UserServiceModel.class;
    }

    @Override
    protected Class<UserResponseModel> getOutputClass() {
        return UserResponseModel.class;
    }
}
