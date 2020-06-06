package com.code.to.learn.persistence.service.impl;

import com.code.to.learn.persistence.dao.api.HomeworkDao;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

public class HomeworkServiceImplTest {

    @Mock
    private ModelMapper modelMapper;
    @Mock
    private HomeworkDao homeworkDao;
    private HomeworkServiceImpl homeworkService;

    public HomeworkServiceImplTest() {
        MockitoAnnotations.initMocks(this);
    }

    @BeforeEach
    public void setUp() {
        homeworkService = new HomeworkServiceImpl(homeworkDao, modelMapper);
    }


}
