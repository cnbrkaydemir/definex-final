package com.cnbrkaydemir.tasks.controller;

import com.cnbrkaydemir.tasks.dto.*;
import com.cnbrkaydemir.tasks.config.GlobalExceptionHandler;
import com.cnbrkaydemir.tasks.exception.notfound.UserNotFoundException;
import com.cnbrkaydemir.tasks.factory.CommentTestDataFactory;
import com.cnbrkaydemir.tasks.factory.ProjectTestDataFactory;
import com.cnbrkaydemir.tasks.factory.TeamTestDataFactory;
import com.cnbrkaydemir.tasks.factory.UsersTestDataFactory;
import com.cnbrkaydemir.tasks.model.Users;
import com.cnbrkaydemir.tasks.service.UsersService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class UsersControllerTest {

    private final String BASE_PATH = "/api/user";

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private UsersController usersController;

    @Mock
    private UsersService usersService;

    private ObjectMapper objectMapper;

    private ModelMapper modelMapper;

    private Users user;

    private UserDto userDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(usersController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        user = UsersTestDataFactory
                .createCustomUser("Canberk", "Aydemir", "c@gmail.com", "password");

        modelMapper = new ModelMapper();

        userDto = modelMapper.map(user, UserDto.class);

        objectMapper = new ObjectMapper();
    }

    @Test
    void findAll_ShouldReturnOk() throws Exception {
        when(usersService.getAll()).thenReturn(List.of(userDto));

        String apiPath = BASE_PATH + "/v1";
        mockMvc.perform(get(apiPath))
                .andExpect(status().isOk());
    }

    @Test
    void findById_ShouldReturnOk() throws Exception {
        when(usersService.get(user.getId())).thenReturn(userDto);

        String apiPath = BASE_PATH + "/v1/"+user.getId();
        mockMvc.perform(get(apiPath))
                .andExpect(status().isOk());

    }

    @Test
    void findById_ShouldReturnNotFound() throws Exception {
        UUID randomId = UUID.randomUUID();
        when(usersService.get(randomId))
                .thenThrow(new UserNotFoundException(randomId));

        String apiPath = BASE_PATH + "/v1/" + randomId.toString();
        mockMvc.perform(get(apiPath))
                .andExpect(status().isNotFound());
    }

    @Test
    void createUser_ShouldReturnCreated() throws Exception {
        when(usersService.create(user)).thenReturn(userDto);

        String apiPath = BASE_PATH + "/v1";
        mockMvc.perform(post(apiPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated());

    }

    @Test
    void updateUser_ShouldReturnOk() throws Exception {
        userDto.setId(UUID.randomUUID());
        userDto.setPhoneNumber("+49 000");
        when(usersService.update(userDto.getId(), userDto)).thenReturn(userDto);

        String apiPath = BASE_PATH + "/v1/"+userDto.getId();
        mockMvc.perform(patch(apiPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk());
    }

    @Test
    void updateUser_ShouldReturnNotFound() throws Exception {
        UUID randomId = UUID.randomUUID();
        when(usersService.update(randomId, userDto))
                .thenThrow(new UserNotFoundException(randomId));

        String apiPath = BASE_PATH + "/v1/"+randomId.toString();
        mockMvc.perform(patch(apiPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteUser_ShouldReturnNoContent() throws Exception {
        when(usersService.delete(user.getId())).thenReturn(true);

        String apiPath = BASE_PATH + "/v1/"+user.getId();
        mockMvc.perform(delete(apiPath))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteUser_ShouldReturnNotFound() throws Exception {
        UUID randomId = UUID.randomUUID();
        when(usersService.delete(randomId))
                .thenThrow(new UserNotFoundException(randomId));

        String apiPath = BASE_PATH + "/v1/"+randomId;
        mockMvc.perform(delete(apiPath))
                .andExpect(status().isNotFound());
    }

    @Test
    void findUserTeams_ShouldReturnOk() throws Exception {
        when(usersService.getUserTeams(user.getId()))
                .thenReturn(Stream.of(TeamTestDataFactory.createDefaultTeam())
                        .map(team -> modelMapper.map(team, TeamDto.class))
                        .toList());

        String apiPath = BASE_PATH + "/v1/"+user.getId()+"/teams";
        mockMvc.perform(get(apiPath))
                .andExpect(status().isOk());

    }

    @Test
    void findUserTeams_ShouldReturnNotFound() throws Exception {
        UUID randomId = UUID.randomUUID();
        when(usersService.getUserTeams(randomId))
                .thenThrow(new UserNotFoundException(randomId));

        String apiPath = BASE_PATH + "/v1/"+randomId+"/teams";
        mockMvc.perform(get(apiPath))
                .andExpect(status().isNotFound());
    }

    @Test
    void findUserProjects_ShouldReturnOk() throws Exception {
        when(usersService.getUserProjects(user.getId()))
                .thenReturn(Stream.of(ProjectTestDataFactory.createDefaultProject())
                        .map(task -> modelMapper.map(task, ProjectDto.class))
                        .toList());

        String apiPath = BASE_PATH + "/v1/"+user.getId()+"/projects";
        mockMvc.perform(get(apiPath))
                .andExpect(status().isOk());
    }

    @Test
    void findUserProjects_ShouldReturnNotFound() throws Exception {
        UUID randomId = UUID.randomUUID();
        when(usersService.getUserProjects(randomId))
                .thenThrow(new UserNotFoundException(randomId));

        String apiPath = BASE_PATH + "/v1/"+randomId+"/projects";
        mockMvc.perform(get(apiPath))
                .andExpect(status().isNotFound());
    }

    @Test
    void findUserComments_ShouldReturnOk() throws Exception {
        when(usersService.getUserComments(user.getId()))
                .thenReturn(Stream.of(CommentTestDataFactory.createComment())
                        .map(comment -> modelMapper.map(comment, CommentDto.class))
                        .toList());

        String apiPath = BASE_PATH + "/v1/"+user.getId()+"/comments";
        mockMvc.perform(get(apiPath))
                .andExpect(status().isOk());
    }

    @Test
    void findUserComments_ShouldReturnNotFound() throws Exception {
        UUID randomId = UUID.randomUUID();
        when(usersService.getUserComments(randomId))
                .thenThrow(new UserNotFoundException(randomId));

        String apiPath = BASE_PATH + "/v1/"+randomId+"/comments";
        mockMvc.perform(get(apiPath))
                .andExpect(status().isNotFound());
    }

    @Test
    void findUserTasks_ShouldReturnOk() throws Exception {
        when(usersService.getUserTasks(user.getId()))
                .thenReturn(user.getTasks()
                        .stream()
                        .map(task -> modelMapper.map(task, TaskDto.class))
                        .toList());

        String apiPath = BASE_PATH + "/v1/"+user.getId()+"/tasks";
        mockMvc.perform(get(apiPath))
                .andExpect(status().isOk());

    }

    @Test
    void findUserTasks_ShouldReturnNotFound() throws Exception {
        UUID randomId = UUID.randomUUID();
        when(usersService.getUserTasks(randomId))
                .thenThrow(new UserNotFoundException(randomId));

        String apiPath = BASE_PATH + "/v1/"+randomId+"/tasks";
        mockMvc.perform(get(apiPath))
                .andExpect(status().isNotFound());
    }

}
