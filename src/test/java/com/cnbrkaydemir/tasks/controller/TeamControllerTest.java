package com.cnbrkaydemir.tasks.controller;

import com.cnbrkaydemir.tasks.dto.CreateTeamDto;
import com.cnbrkaydemir.tasks.dto.TeamDto;
import com.cnbrkaydemir.tasks.exception.GlobalExceptionHandler;
import com.cnbrkaydemir.tasks.exception.notfound.TeamNotFoundException;
import com.cnbrkaydemir.tasks.factory.TeamTestDataFactory;
import com.cnbrkaydemir.tasks.model.Team;
import com.cnbrkaydemir.tasks.service.TeamService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TeamControllerTest {
    private final String BASE_PATH = "/api/team";

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private TeamController teamController;

    @Mock
    private TeamService teamService;

    private ObjectMapper objectMapper;

    private ModelMapper modelMapper;

    private Team team;

    private TeamDto teamDto;

    private CreateTeamDto createTeamDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(teamController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        modelMapper = new ModelMapper();

        team = TeamTestDataFactory.createDefaultTeam();

        teamDto = modelMapper.map(team, TeamDto.class);

        createTeamDto = modelMapper.map(teamDto, CreateTeamDto.class);
        createTeamDto.setDepartment(team.getDepartment().getId());

        objectMapper = new ObjectMapper();
    }

    @Test
    void findAllTeams_ShouldReturnOk() throws Exception {
        when(teamService.getAllTeams()).thenReturn(List.of(teamDto));

        String apiPath = BASE_PATH + "/v1";
        mockMvc.perform(get(apiPath))
                .andExpect(status().isOk());
    }

    @Test
    void findTeamById_ShouldReturnOk() throws Exception {
        when(teamService.getTeamById(team.getId())).thenReturn(teamDto);

        String apiPath = BASE_PATH + "/v1/"+team.getId();
        mockMvc.perform(get(apiPath))
                .andExpect(status().isOk());
    }

    @Test
    void findTeamById_ShouldReturnNotFound() throws Exception {
        UUID randomId = UUID.randomUUID();
        when(teamService.getTeamById(randomId))
                .thenThrow(new TeamNotFoundException(randomId));

        String apiPath = BASE_PATH + "/v1/" + randomId.toString();
        mockMvc.perform(get(apiPath))
                .andExpect(status().isNotFound());
    }

    @Test
    void createTeam_ShouldReturnCreated() throws Exception {
        when(teamService.createTeam(createTeamDto)).thenReturn(teamDto);

        String apiPath = BASE_PATH + "/v1";
        mockMvc.perform(post(apiPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createTeamDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void updateTeam_ShouldReturnOk() throws Exception {
        teamDto.setId(UUID.randomUUID());
        teamDto.setGoal("Win Win Win");
        when(teamService.updateTeam(teamDto.getId(), teamDto)).thenReturn(teamDto);

        String apiPath = BASE_PATH + "/v1/"+team.getId();
        mockMvc.perform(patch(apiPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teamDto)))
                .andExpect(status().isOk());
    }

    @Test
    void updateTeam_ShouldReturnNotFound() throws Exception {
        UUID randomId = UUID.randomUUID();
        when(teamService.updateTeam(randomId, teamDto))
                .thenThrow(new TeamNotFoundException(randomId));

        String apiPath = BASE_PATH + "/v1/"+randomId;
        mockMvc.perform(patch(apiPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teamDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteTeam_ShouldReturnNoContent() throws Exception {
        when(teamService.deleteTeam(team.getId())).thenReturn(true);

        String apiPath = BASE_PATH + "/v1/" +team.getId();
        mockMvc.perform(delete(apiPath))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteTeam_ShouldReturnNotFound() throws Exception {
        UUID randomId = UUID.randomUUID();
        when(teamService.deleteTeam(randomId))
                .thenThrow(new TeamNotFoundException(randomId));

        String apiPath = BASE_PATH + "/v1/" +randomId;
        mockMvc.perform(delete(apiPath))
                .andExpect(status().isNotFound());
    }


}
