package com.code.to.learn.persistence.service.impl;

import com.code.to.learn.persistence.dao.api.CommentDao;
import com.code.to.learn.persistence.domain.model.CommentServiceModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;

public class CommentServiceImplTest {

    @Mock
    private ModelMapper modelMapper;
    @Mock
    private CommentDao commentDao;
    private CommentServiceImpl commentService;

    public CommentServiceImplTest() {
        MockitoAnnotations.initMocks(this);
    }

    @BeforeEach
    public void setUp() {
        commentService = new CommentServiceImpl(commentDao, modelMapper);
    }

    @Test
    public void testFindCommentsByCourseName() {
        Mockito.when(commentDao.findAllByCourseName("123")).thenReturn(Collections.emptyList());
        List<CommentServiceModel> commentsByCourseName = commentService.findCommentsByCourseName("123");
        Assertions.assertEquals(0, commentsByCourseName.size());
    }

}
