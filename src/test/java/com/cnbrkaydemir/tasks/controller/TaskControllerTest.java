package com.cnbrkaydemir.tasks.controller;

import com.cnbrkaydemir.tasks.exception.GlobalExceptionHandler;
import com.cnbrkaydemir.tasks.factory.TaskTestDataFactory;
import com.cnbrkaydemir.tasks.model.Task;
import com.cnbrkaydemir.tasks.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class TaskControllerTest {
    private final String BASE_PATH = "/api/task/v1";

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private TaskController taskController;

    @Mock
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    private Task task;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(taskController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        task = TaskTestDataFactory.createDefaultTask();

        objectMapper = new ObjectMapper();
    }

}
