package com.code.to.learn.persistence.service.impl;

import com.code.to.learn.persistence.dao.api.ContactUsDao;
import com.code.to.learn.persistence.service.api.ContactUsService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

public class ContactUsServiceImplTest {

    @Mock
    private ModelMapper modelMapper;
    @Mock
    private ContactUsDao contactUsDao;
    private ContactUsService contactUsService;

    public ContactUsServiceImplTest() {
        MockitoAnnotations.initMocks(this);
    }

    @BeforeEach
    public void setUp() {
        contactUsService = new ContactUsServiceImpl(contactUsDao, modelMapper);
    }


}
