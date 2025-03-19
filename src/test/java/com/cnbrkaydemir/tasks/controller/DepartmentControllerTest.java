package com.cnbrkaydemir.tasks.controller;

import com.cnbrkaydemir.tasks.exception.GlobalExceptionHandler;
import com.cnbrkaydemir.tasks.factory.DepartmentTestDataFactory;
import com.cnbrkaydemir.tasks.model.Department;
import com.cnbrkaydemir.tasks.service.DepartmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class DepartmentControllerTest {

    private final String BASE_PATH = "/api/department/v1";

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private DepartmentController departmentController;

    @Mock
    private DepartmentService departmentService;

    @Autowired
    private ObjectMapper objectMapper;

    private Department department;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(departmentController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        department = DepartmentTestDataFactory.createCustomDepartment("Application Development", "Developing Applications....");

        objectMapper = new ObjectMapper();
    }

}
