package com.code.to.learn.persistence.service.impl;

import com.code.to.learn.persistence.dao.api.CommentDao;
import com.code.to.learn.persistence.domain.entity.Comment;
import com.code.to.learn.persistence.domain.model.CommentServiceModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;

public class CommentServiceImplTest {

    @Spy
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
        Comment comment = new Comment();
        comment.setContent("123");
        List<Comment> comments = Collections.singletonList(comment);
        Mockito.when(commentDao.findAllByCourseName("123")).thenReturn(comments);
        List<CommentServiceModel> commentsByCourseName = commentService.findCommentsByCourseName("123");
        Assertions.assertEquals(1, commentsByCourseName.size());
        Assertions.assertEquals("123", commentsByCourseName.get(0).getContent());
    }

}
