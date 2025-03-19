package com.cnbrkaydemir.tasks.controller;

import com.cnbrkaydemir.tasks.exception.GlobalExceptionHandler;
import com.cnbrkaydemir.tasks.factory.UsersTestDataFactory;
import com.cnbrkaydemir.tasks.model.Users;
import com.cnbrkaydemir.tasks.service.UsersService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


public class UsersControllerTest {

    private final String BASE_PATH = "/api/users/v1";

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private UsersController usersController;

    @Mock
    private UsersService usersService;

    @Autowired
    private ObjectMapper objectMapper;

    private Users user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(usersController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        user = UsersTestDataFactory
                .createCustomUser("Canberk", "Aydemir", "c@gmail.com", "password");

        objectMapper = new ObjectMapper();
    }



}
