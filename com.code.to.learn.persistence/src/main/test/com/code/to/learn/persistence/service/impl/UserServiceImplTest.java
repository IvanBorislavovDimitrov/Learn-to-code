package com.code.to.learn.persistence.service.impl;

import com.code.to.learn.persistence.dao.api.UserDao;
import com.code.to.learn.persistence.domain.entity.User;
import com.code.to.learn.persistence.domain.model.UserChangePasswordServiceModel;
import com.code.to.learn.persistence.domain.model.UserForgottenPasswordServiceModel;
import com.code.to.learn.persistence.domain.model.UserServiceModel;
import com.code.to.learn.persistence.service.api.UserService;
import com.code.to.learn.persistence.util.ResetPasswordTokenGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;

public class UserServiceImplTest {

    @Mock
    private ModelMapper modelMapper;
    @Mock
    private UserDao userDao;
    @Mock
    private ResetPasswordTokenGenerator resetPasswordTokenGenerator;
    private UserService userService;

    public UserServiceImplTest() {
        MockitoAnnotations.initMocks(this);
    }

    @BeforeEach
    public void setUp() {
        userService = new UserServiceImpl(userDao, modelMapper, resetPasswordTokenGenerator);
    }

    @Test
    public void testRegisterUser() {
        UserServiceModel userServiceModel = Mockito.mock(UserServiceModel.class);
        User user = Mockito.mock(User.class);
        Mockito.when(modelMapper.map(userServiceModel, User.class)).thenReturn(user);
        userService.registerUser(userServiceModel);
        Mockito.verify(userDao).persist(user);
    }

    @Test
    public void testIsUsernameTaken() {
        Mockito.when(userDao.findByUsername("123")).thenReturn(Optional.empty());
        Assertions.assertFalse(userService.isUsernameTaken("123"));
    }

    @Test
    public void testIsEmailTaken() {
        Mockito.when(userDao.findByEmail("123")).thenReturn(Optional.empty());
        Assertions.assertFalse(userService.isEmailTaken("123"));
    }

    @Test
    public void testIsPhoneNumberTaken() {
        Mockito.when(userDao.findByPhoneNumber("123")).thenReturn(Optional.empty());
        Assertions.assertFalse(userService.isPhoneNumberTaken("123"));
    }

    @Test
    public void testFindByUsername() {
        User user = Mockito.mock(User.class);
        Mockito.when(userDao.findByUsername(anyString())).thenReturn(Optional.of(user));
        UserServiceModel userServiceModel = Mockito.mock(UserServiceModel.class);
        Mockito.when(userServiceModel.getUsername()).thenReturn("123");
        Mockito.when(modelMapper.map(user, UserServiceModel.class)).thenReturn(userServiceModel);
        UserServiceModel userServiceByUsername = userService.findByUsername("123");
        Assertions.assertEquals("123", userServiceByUsername.getUsername());
    }

    @Test
    public void testFindUsersCount() {
        Mockito.when(userDao.count()).thenReturn(1L);
        Assertions.assertEquals(1L, userService.count());
    }

    @Test
    public void testFindUsersByUsernameContaining() {
        Mockito.when(modelMapper.map(Collections.emptyList(), List.class)).thenReturn(Collections.emptyList());
        List<UserServiceModel> usersByUsernameContaining = userService.findUsersByUsernameContaining("123");
        Assertions.assertEquals(0, usersByUsernameContaining.size());
    }

    @Test
    public void testFindTeachers() {
        Mockito.when(modelMapper.map(Collections.emptyList(), List.class)).thenReturn(Collections.emptyList());
        List<UserServiceModel> teachers = userService.findTeachers();
        Assertions.assertEquals(0, teachers.size());
    }

    @Test
    public void testActivateAccount() {
        User user = Mockito.mock(User.class);
        Mockito.when(userDao.findByUsername(anyString())).thenReturn(Optional.of(user));
        UserServiceModel userServiceModel = Mockito.mock(UserServiceModel.class);
        Mockito.when(userServiceModel.getUsername()).thenReturn("123");
        Mockito.when(modelMapper.map(user, UserServiceModel.class)).thenReturn(userServiceModel);
        Mockito.when(modelMapper.map(userServiceModel, User.class)).thenReturn(user);
        Mockito.when(userDao.findById(anyString())).thenReturn(Optional.of(user));
        Mockito.when(userDao.update(user)).thenReturn(Optional.of(user));
        Mockito.when(user.merge(any())).thenReturn(user);
        UserServiceModel activateAccount = userService.activateAccount("123");
        Assertions.assertNotNull(activateAccount);
    }

    @Test
    public void testGenerateResetPasswordToken() {
        Mockito.when(resetPasswordTokenGenerator.generateResetPasswordToken()).thenReturn("123");
        User user = Mockito.mock(User.class);
        Mockito.when(userDao.findByUsername(anyString())).thenReturn(Optional.of(user));
        UserServiceModel userServiceModel = Mockito.mock(UserServiceModel.class);
        Mockito.when(userServiceModel.getUsername()).thenReturn("123");
        UserForgottenPasswordServiceModel userForgottenPasswordServiceModel = userService.generateResetPasswordToken("user");
        Assertions.assertEquals("123", userForgottenPasswordServiceModel.getToken());
        Mockito.verify(userDao).update(user);
    }

    @Test
    public void testChangeForgottenPassword() {
        User user = Mockito.mock(User.class);
        Mockito.when(user.getResetPasswordToken()).thenReturn("123");
        Mockito.when(userDao.findByResetPasswordToken(anyString())).thenReturn(Optional.of(user));
        UserChangePasswordServiceModel userChangePasswordServiceModel = Mockito.mock(UserChangePasswordServiceModel.class);
        Mockito.when(userChangePasswordServiceModel.getToken()).thenReturn("123");
        Mockito.when(userDao.update(user)).thenReturn(Optional.of(user));
        UserServiceModel userServiceModel = Mockito.mock(UserServiceModel.class);
        Mockito.when(userServiceModel.getUsername()).thenReturn("123");
        Mockito.when(modelMapper.map(user, UserServiceModel.class)).thenReturn(userServiceModel);
        UserServiceModel changePassword = userService.changeForgottenPassword(userChangePasswordServiceModel);
        Assertions.assertEquals("123", changePassword.getUsername());
    }

    @Test
    public void testStoreUserLoginInformation() {
        User user = Mockito.mock(User.class);
        Mockito.when(userDao.findByUsername(anyString())).thenReturn(Optional.of(user));
        UserServiceModel userServiceModel = Mockito.mock(UserServiceModel.class);
        Mockito.when(userServiceModel.getUsername()).thenReturn("123");
        userService.storeUserLoginInformation("123", LocalDate.now(), "no");
        Mockito.verify(userDao).update(user);
    }

}
