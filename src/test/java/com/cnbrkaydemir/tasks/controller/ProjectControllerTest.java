package com.cnbrkaydemir.tasks.controller;

import com.cnbrkaydemir.tasks.exception.GlobalExceptionHandler;
import com.cnbrkaydemir.tasks.factory.ProjectTestDataFactory;
import com.cnbrkaydemir.tasks.model.Project;
import com.cnbrkaydemir.tasks.service.ProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class ProjectControllerTest {
    private final String BASE_PATH = "/api/project/v1";

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private ProjectController projectController;

    @Mock
    private ProjectService projectService;

    @Autowired
    private ObjectMapper objectMapper;

    private Project project;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(projectController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        project = ProjectTestDataFactory.createDefaultProject();

        objectMapper = new ObjectMapper();
    }

}
