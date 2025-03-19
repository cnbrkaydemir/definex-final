package com.cnbrkaydemir.tasks.controller;

import com.cnbrkaydemir.tasks.exception.GlobalExceptionHandler;
import com.cnbrkaydemir.tasks.factory.CommentTestDataFactory;
import com.cnbrkaydemir.tasks.model.Comment;
import com.cnbrkaydemir.tasks.service.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class CommentControllerTest {

    private final String BASE_PATH = "/api/comment/v1";

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private CommentController commentController;

    @Mock
    private CommentService commentService;

    @Autowired
    private ObjectMapper objectMapper;

    private Comment comment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(commentController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        comment = CommentTestDataFactory.createComment();

        objectMapper = new ObjectMapper();
    }

}
