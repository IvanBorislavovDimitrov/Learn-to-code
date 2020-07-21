package com.code.to.learn.web.api_facade;

import com.code.to.learn.api.api.user.UserServiceApi;
import com.code.to.learn.api.jwt.JwtUtil;
import com.code.to.learn.api.model.authentication.JwtTokenResponse;
import com.code.to.learn.api.model.user.*;
import com.code.to.learn.core.environment.ApplicationConfiguration;
import com.code.to.learn.core.validator.UserValidator;
import com.code.to.learn.persistence.domain.entity.entity_enum.UserRole;
import com.code.to.learn.persistence.domain.model.RoleServiceModel;
import com.code.to.learn.persistence.domain.model.UserChangePasswordServiceModel;
import com.code.to.learn.persistence.domain.model.UserForgottenPasswordServiceModel;
import com.code.to.learn.persistence.domain.model.UserServiceModel;
import com.code.to.learn.persistence.service.api.RoleService;
import com.code.to.learn.persistence.service.api.UserService;
import com.code.to.learn.util.mapper.ExtendableMapper;
import com.code.to.learn.web.constants.Messages;
import com.code.to.learn.web.mail.Email;
import com.code.to.learn.web.mail.EmailClient;
import com.code.to.learn.web.util.FileToUpload;
import com.code.to.learn.web.util.RemoteStorageFileOperator;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.code.to.learn.api.constant.Constants.DATE_PATTERN;
import static com.code.to.learn.web.constants.Constants.DEFAULT_PICTURE_NAME;
import static com.code.to.learn.web.constants.Constants.PROFILE_PICTURE_EXTENSION;

@Service
public class UserServiceApiFacade extends ExtendableMapper<UserServiceModel, UserResponseModel> implements UserServiceApi {

    private final UserValidator userValidator;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final RemoteStorageFileOperator remoteStorageFileOperator;
    private final EmailClient emailClient;
    private final ApplicationConfiguration applicationConfiguration;

    @Autowired
    public UserServiceApiFacade(ModelMapper modelMapper, UserValidator userValidator, UserService userService,
                                PasswordEncoder passwordEncoder, RoleService roleService, RemoteStorageFileOperator remoteStorageFileOperator, EmailClient emailClient, ApplicationConfiguration applicationConfiguration) {
        super(modelMapper);
        this.userValidator = userValidator;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.remoteStorageFileOperator = remoteStorageFileOperator;
        this.emailClient = emailClient;
        this.applicationConfiguration = applicationConfiguration;
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
        if (userBindingModel.getProfilePicture() != null) {
            remoteStorageFileOperator.uploadFileAsync(new FileToUpload(userServiceModel.getProfilePictureName(), userBindingModel.getProfilePicture()));
        }
        addRolesForUser(userServiceModel);
        convertAndSetDateToUser(userBindingModel, userServiceModel);
        userService.registerUser(userServiceModel);
        sendActivationEmail(userBindingModel);
        UserResponseModel userResponseModel = toOutput(userServiceModel);
        return ResponseEntity.ok(userResponseModel);
    }

    private void sendActivationEmail(UserBindingModel userBindingModel) {
        String activateAccountUrl = applicationConfiguration.getActivateAccountUrl();
        activateAccountUrl = String.format(activateAccountUrl, userBindingModel.getUsername());
        Email activateAccountEmail = new Email.Builder()
                .setContent(MessageFormat.format(Messages.ACTIVATE_ACCOUNT_EMAIL, activateAccountUrl))
                .setTitle(Messages.ACCOUNT_ACTIVATION)
                .setRecipient(userBindingModel.getEmail())
                .build();
        emailClient.sendAsync(activateAccountEmail);
    }

    @Override
    public ResponseEntity<List<UserResponseModel>> findUsersByUsernameContaining(String username) {
        List<UserServiceModel> userServiceModels = userService.findUsersByUsernameContaining(username);
        return ResponseEntity.ok(toOutput(userServiceModels));
    }

    @Override
    public ResponseEntity<UserResponseModel> changeUserRoles(String username, List<String> roles) {
        UserServiceModel userServiceModel = userService.findByUsername(username);
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
        UserServiceModel userServiceModel = userService.findByUsername(username);
        return toOutput(userServiceModel);
    }

    private List<RoleServiceModel> toRoleServiceModels(List<String> roles) {
        return roles.stream()
                .map(roleService::findByName)
                .collect(Collectors.toList());
    }

    private UserServiceModel toUserServiceModel(UserBindingModel userBindingModel) {
        UserBindingModel encodedPasswordUserBindingModel = getUserBindingModelWithEncodedPassword(userBindingModel);
        UserServiceModel userServiceModel = getMapper().map(encodedPasswordUserBindingModel, UserServiceModel.class);
        userServiceModel.setProfilePictureName(getProfilePictureName(userBindingModel.getUsername(), userBindingModel.getProfilePicture()));
        return userServiceModel;
    }

    private String getProfilePictureName(String username, MultipartFile profilePicture) {
        if (profilePicture == null) {
            return DEFAULT_PICTURE_NAME;
        }
        String extension = FilenameUtils.getExtension(profilePicture.getOriginalFilename());
        return username + PROFILE_PICTURE_EXTENSION + "." + extension;
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
            RoleServiceModel roleServiceModel = roleService.findByName(UserRole.ROLE_USER.toString());
            userServiceModel.setRoles(Collections.singletonList(roleServiceModel));
        }
    }

    private void convertAndSetDateToUser(UserBindingModel userBindingModel, UserServiceModel userServiceModel) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
        LocalDate birthDate = LocalDate.parse(userBindingModel.getBirthDate(), formatter);
        userServiceModel.setBirthDate(birthDate);
    }

    @Override
    public ResponseEntity<List<UserResponseModel>> findTeachers() {
        List<UserServiceModel> userServiceModels = userService.findTeachers();
        return ResponseEntity.ok(toOutput(userServiceModels));
    }

    @Override
    public ResponseEntity<UserResponseModel> activateAccount(String username) {
        UserServiceModel userServiceModel = userService.activateAccount(username);
        return ResponseEntity.ok(toOutput(userServiceModel));
    }

    @Override
    public ResponseEntity<?> sendEmailForPasswordReset(String username) {
        UserForgottenPasswordServiceModel userForgottenPasswordServiceModel = userService.generateResetPasswordToken(username);
        sendEmailForPasswordReset(userForgottenPasswordServiceModel);
        return ResponseEntity.ok().build();
    }

    private void sendEmailForPasswordReset(UserForgottenPasswordServiceModel userForgottenPasswordServiceModel) {
        Email passwordResetEmail = new Email.Builder()
                .setContent(MessageFormat.format(Messages.RESET_YOUR_PASSWORD,
                        generateUserForgottenPasswordLink(userForgottenPasswordServiceModel.getToken())))
                .setRecipient(userForgottenPasswordServiceModel.getEmail())
                .setTitle(Messages.FORGOTTEN_PASSWORD)
                .build();
        emailClient.sendAsync(passwordResetEmail);
    }

    private String generateUserForgottenPasswordLink(String forgottenPasswordToken) {
        return String.format(applicationConfiguration.getResetPasswordUrl(), forgottenPasswordToken);
    }

    @Override
    public ResponseEntity<UserResponseModel> changeForgottenPassword(UserChangePasswordBindingModel userChangePasswordBindingModel) {
        userValidator.validatePasswordsMatch(userChangePasswordBindingModel);
        UserChangePasswordServiceModel userChangePasswordServiceModel = getMapper()
                .map(userChangePasswordBindingModel, UserChangePasswordServiceModel.class);
        userChangePasswordServiceModel.setPassword(passwordEncoder.encode(userChangePasswordBindingModel.getPassword()));
        userChangePasswordServiceModel.setConfirmPassword(passwordEncoder.encode(userChangePasswordBindingModel.getConfirmPassword()));
        UserResponseModel userResponseModel = toOutput(userService.changeForgottenPassword(userChangePasswordServiceModel));
        return ResponseEntity.ok(userResponseModel);
    }

    @Override
    public ResponseEntity<UserResponseModel> updateProfilePicture(String username, MultipartFile profilePicture) {
        UserServiceModel userServiceModel = userService.findByUsername(username);
        remoteStorageFileOperator.removeFileSync(userServiceModel.getProfilePictureName());
        userServiceModel.setProfilePictureName(getProfilePictureName(username, profilePicture));
        remoteStorageFileOperator.uploadFileSync(new FileToUpload(userServiceModel.getProfilePictureName(), profilePicture));
        UserServiceModel updatedUserServiceModel = userService.update(userServiceModel);
        return ResponseEntity.ok(toOutput(updatedUserServiceModel));
    }

    @Override
    public ResponseEntity<UserResponseModel> updateBasicProfileInformation(String username,
                                                                           UserBasicUpdateBindingModel userBasicUpdateBindingModel) {
        UserServiceModel userServiceModel = userService.findByUsername(username);
        userServiceModel = updateBasicUserServiceModel(userServiceModel, userBasicUpdateBindingModel);
        UserServiceModel updateUserServiceModel = userService.update(userServiceModel);
        return ResponseEntity.ok(toOutput(updateUserServiceModel));
    }

    private UserServiceModel updateBasicUserServiceModel(UserServiceModel userServiceModel,
                                                         UserBasicUpdateBindingModel userBasicUpdateBindingModel) {
        userServiceModel.setFirstName(userBasicUpdateBindingModel.getFirstName());
        userServiceModel.setLastName(userBasicUpdateBindingModel.getLastName());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
        LocalDate birthDate = LocalDate.parse(userBasicUpdateBindingModel.getBirthDate(), formatter);
        userServiceModel.setBirthDate(birthDate);
        userServiceModel.setDescription(userBasicUpdateBindingModel.getDescription());
        return userServiceModel;
    }

    @Override
    public ResponseEntity<UserResponseModel> deactivateProfile(String username) {
        UserServiceModel userServiceModel = userService.deactivateUserProfile(username);
        return ResponseEntity.ok(toOutput(userServiceModel));
    }

    @Override
    public ResponseEntity<?> changeUserPassword(UserPasswordChangeBindingModel userPasswordChangeBindingModel) {
        userValidator.validateCurrentPasswordMatch(userPasswordChangeBindingModel.getUsername(), userPasswordChangeBindingModel.getCurrentPassword());
        userValidator.validatePasswordsMatch(userPasswordChangeBindingModel);
        UserServiceModel userServiceModel = userService.findByUsername(userPasswordChangeBindingModel.getUsername());
        userServiceModel.setPassword(passwordEncoder.encode(userPasswordChangeBindingModel.getNewPassword()));
        userService.update(userServiceModel);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<JwtTokenResponse> generateTokenForUser(String username) {
        UserDetails userDetails = userService.findByUsername(username);
        String jwt = JwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtTokenResponse(jwt));
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
