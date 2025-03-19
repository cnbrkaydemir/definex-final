package com.cnbrkaydemir.tasks.controller;

import com.cnbrkaydemir.tasks.exception.GlobalExceptionHandler;
import com.cnbrkaydemir.tasks.factory.AttachmentTestDataFactory;
import com.cnbrkaydemir.tasks.model.Attachment;
import com.cnbrkaydemir.tasks.service.AttachmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class AttachmentControllerTest {

    private final String BASE_PATH = "/api/attachment/v1";

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private AttachmentController attachmentController;

    @Mock
    private AttachmentService attachmentService;

    @Autowired
    private ObjectMapper objectMapper;

    private Attachment attachment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(attachmentController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        attachment = AttachmentTestDataFactory.createAttachment();

        objectMapper = new ObjectMapper();
    }

}
