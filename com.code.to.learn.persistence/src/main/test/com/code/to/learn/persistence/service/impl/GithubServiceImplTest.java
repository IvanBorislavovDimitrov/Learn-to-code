package com.code.to.learn.persistence.service.impl;

import com.code.to.learn.persistence.dao.api.GithubDao;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

public class GithubServiceImplTest {

    @Mock
    private ModelMapper modelMapper;
    @Mock
    private GithubDao githubDao;
    private GithubServiceImpl githubService;

    public GithubServiceImplTest() {
        MockitoAnnotations.initMocks(this);
    }

    @BeforeEach
    public void setUp() {
        githubService = new GithubServiceImpl(githubDao, modelMapper);
    }


}
