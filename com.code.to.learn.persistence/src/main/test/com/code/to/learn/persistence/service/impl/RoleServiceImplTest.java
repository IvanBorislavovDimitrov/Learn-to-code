package com.code.to.learn.persistence.service.impl;

import com.code.to.learn.persistence.dao.api.RoleDao;
import com.code.to.learn.persistence.domain.entity.Role;
import com.code.to.learn.persistence.domain.entity.entity_enum.UserRole;
import com.code.to.learn.persistence.domain.model.RoleServiceModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.Optional;

public class RoleServiceImplTest {

    @Mock
    private ModelMapper modelMapper;
    @Mock
    private RoleDao roleDao;
    private RoleServiceImpl roleService;

    public RoleServiceImplTest() {
        MockitoAnnotations.initMocks(this);
    }

    @BeforeEach
    public void setUp() {
        roleService = new RoleServiceImpl(roleDao, modelMapper);
    }

    @Test
    public void testFindByName() {
        Role role = Mockito.mock(Role.class);
        Mockito.when(role.getName()).thenReturn(UserRole.ROLE_ADMIN);
        Mockito.when(roleDao.findByName(UserRole.ROLE_ADMIN)).thenReturn(Optional.of(role));
        RoleServiceModel roleServiceModel = Mockito.mock(RoleServiceModel.class);
        Mockito.when(roleServiceModel.getName()).thenReturn("ROLE_ADMIN");
        Mockito.when(modelMapper.map(role, RoleServiceModel.class)).thenReturn(roleServiceModel);
        RoleServiceModel admin = roleService.findByName("ROLE_ADMIN");
        Assertions.assertEquals("ROLE_ADMIN", admin.getName());
    }

}
