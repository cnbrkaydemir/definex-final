package com.cnbrkaydemir.tasks.controller;

import com.cnbrkaydemir.tasks.exception.GlobalExceptionHandler;
import com.cnbrkaydemir.tasks.factory.TeamTestDataFactory;
import com.cnbrkaydemir.tasks.model.Team;
import com.cnbrkaydemir.tasks.service.TeamService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class TeamControllerTest {
    private final String BASE_PATH = "/api/team/v1";

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private TeamController teamController;

    @Mock
    private TeamService teamService;

    @Autowired
    private ObjectMapper objectMapper;

    private Team team;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(teamController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        team = TeamTestDataFactory.createDefaultTeam();

        objectMapper = new ObjectMapper();
    }

}
