package com.cnbrkaydemir.tasks.controller;

import com.cnbrkaydemir.tasks.dto.*;
import com.cnbrkaydemir.tasks.exception.GlobalExceptionHandler;
import com.cnbrkaydemir.tasks.exception.notfound.ProjectNotFoundException;
import com.cnbrkaydemir.tasks.exception.notfound.TeamNotFoundException;
import com.cnbrkaydemir.tasks.factory.DepartmentTestDataFactory;
import com.cnbrkaydemir.tasks.factory.ProjectTestDataFactory;
import com.cnbrkaydemir.tasks.factory.TaskTestDataFactory;
import com.cnbrkaydemir.tasks.factory.TeamTestDataFactory;
import com.cnbrkaydemir.tasks.model.Department;
import com.cnbrkaydemir.tasks.model.Project;
import com.cnbrkaydemir.tasks.model.Task;
import com.cnbrkaydemir.tasks.model.Team;
import com.cnbrkaydemir.tasks.service.ProjectService;
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

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProjectControllerTest {
    private final String BASE_PATH = "/api/project";

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private ProjectController projectController;

    @Mock
    private ProjectService projectService;

    private ObjectMapper objectMapper;

    private ModelMapper modelMapper;

    private Project project;

    private ProjectDto projectDto;

    private CreateProjectDto projectCreateDto;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(projectController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        modelMapper = new ModelMapper();

        project = ProjectTestDataFactory.createDefaultProject();
        projectDto = modelMapper.map(project, ProjectDto.class);
        projectCreateDto = modelMapper.map(project, CreateProjectDto.class);
        projectCreateDto.setDepartmentId(UUID.randomUUID());

        objectMapper = new ObjectMapper();
    }

    @Test
    void findAllProjects_ShouldReturnOk() throws Exception {
        when(projectService.getProjects()).thenReturn(List.of(projectDto));

        String apiPath = BASE_PATH + "/v1";
        mockMvc.perform(get(apiPath))
                .andExpect(status().isOk());

    }

    @Test
    void findProjectById_ShouldReturnOk() throws Exception {
        when(projectService.getProject(project.getId())).thenReturn(projectDto);

        String apiPath = BASE_PATH + "/v1/"+project.getId();
        mockMvc.perform(get(apiPath))
                .andExpect(status().isOk());

    }

    @Test
    void findProjectById_ShouldReturnNotFound() throws Exception {
        UUID randomId = UUID.randomUUID();
        when(projectService.getProject(randomId))
                .thenThrow(new ProjectNotFoundException(randomId));

        String apiPath = BASE_PATH + "/v1/" + randomId.toString();
        mockMvc.perform(get(apiPath))
                .andExpect(status().isNotFound());

    }

    @Test
    void createProject_ShouldReturnCreated() throws Exception {
        when(projectService.createProject(projectCreateDto)).thenReturn(projectDto);

        String apiPath = BASE_PATH + "/v1";
        mockMvc.perform(post(apiPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectCreateDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void updateProject_ShouldReturnOk() throws Exception {
        projectDto.setId(UUID.randomUUID());
        projectDto.setStartDate(new Date());
        when(projectService.updateProject(projectDto.getId(), projectDto)).thenReturn(projectDto);

        String apiPath = BASE_PATH + "/v1/"+project.getId();
        mockMvc.perform(patch(apiPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectDto)))
                .andExpect(status().isOk());
    }

    @Test
    void updateProject_ShouldReturnNotFound() throws Exception {
        UUID randomId = UUID.randomUUID();
        when(projectService.updateProject(randomId, projectDto))
                .thenThrow(new ProjectNotFoundException(randomId));

        String apiPath = BASE_PATH + "/v1/"+randomId.toString();
        mockMvc.perform(patch(apiPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteProject_ShouldReturnNoContent() throws Exception {
        when(projectService.deleteProject(project.getId())).thenReturn(true);

        String apiPath = BASE_PATH + "/v1/"+project.getId();
        mockMvc.perform(delete(apiPath))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteProject_ShouldReturnNotFound() throws Exception {
        UUID randomId = UUID.randomUUID();
        when(projectService.deleteProject(randomId))
                .thenThrow(new ProjectNotFoundException(randomId));

        String apiPath = BASE_PATH + "/v1/"+randomId.toString();
        mockMvc.perform(delete(apiPath))
                .andExpect(status().isNotFound());
    }

    @Test
    void findProjectDepartment_ShouldReturnOk() throws Exception {
        Department department = DepartmentTestDataFactory.createDefaultDepartment();
        when(projectService.getDepartment(project.getId())).thenReturn(modelMapper.map(department, DepartmentDto.class));

        String apiPath = BASE_PATH + "/v1/"+project.getId()+"/department";
        mockMvc.perform(get(apiPath))
                .andExpect(status().isOk());
    }

    @Test
    void findProjectDepartment_ShouldReturnNotFound() throws Exception {
        UUID randomId = UUID.randomUUID();
        when(projectService.getDepartment(randomId))
                .thenThrow(new ProjectNotFoundException(randomId));

        String apiPath = BASE_PATH + "/v1/"+randomId+"/department";
        mockMvc.perform(get(apiPath))
                .andExpect(status().isNotFound());
    }

    @Test
    void findProjectTeams_ShouldReturnOk() throws Exception {
        Team teams = TeamTestDataFactory.createDefaultTeam();
        when(projectService.getTeams(project.getId()))
                .thenReturn(Stream.of(teams)
                        .map(team -> modelMapper.map(team, TeamDto.class))
                        .toList());

        String apiPath = BASE_PATH + "/v1/"+project.getId()+"/teams";
        mockMvc.perform(get(apiPath))
                .andExpect(status().isOk());
    }

    @Test
    void findProjectTeams_ShouldReturnNotFound() throws Exception {
        UUID randomId = UUID.randomUUID();
        when(projectService.getTeams(randomId))
                .thenThrow(new ProjectNotFoundException(randomId));

        String apiPath = BASE_PATH + "/v1/"+randomId+"/teams";
        mockMvc.perform(get(apiPath))
                .andExpect(status().isNotFound());
    }

    @Test
    void findProjectTasks_ShouldReturnOk() throws Exception {
        Task tasks = TaskTestDataFactory.createDefaultTask();
        when(projectService.getTasks(project.getId()))
                .thenReturn(Stream.of(tasks)
                        .map(task-> modelMapper.map(task, TaskDto.class))
                        .toList());

        String apiPath = BASE_PATH + "/v1/"+project.getId()+"/tasks";
        mockMvc.perform(get(apiPath))
                .andExpect(status().isOk());
    }

    @Test
    void findProjectTasks_ShouldReturnNotFound() throws Exception {
        UUID randomId = UUID.randomUUID();
        when(projectService.getTasks(randomId))
                .thenThrow(new ProjectNotFoundException(randomId));

        String apiPath = BASE_PATH + "/v1/"+randomId+"/tasks";
        mockMvc.perform(get(apiPath))
                .andExpect(status().isNotFound());
    }

    @Test
    void addTeamToProject_ShouldReturnOk() throws Exception {
        Team team = TeamTestDataFactory.createDefaultTeam();
        when(projectService.addTeam(project.getId(), team.getId())).thenReturn(modelMapper.map(team, TeamDto.class));

        String apiPath = BASE_PATH + "/v1/"+project.getId()+"/team/"+team.getId()+"/add";
        mockMvc.perform(post(apiPath))
                .andExpect(status().isOk());
    }

    @Test
    void addTeamToProject_ShouldReturnNotFound() throws Exception {
        UUID randomId = UUID.randomUUID();
        when(projectService.addTeam(project.getId(), randomId))
                .thenThrow(new TeamNotFoundException(randomId));

        String apiPath = BASE_PATH + "/v1/"+project.getId()+"/team/"+randomId+"/add";
        mockMvc.perform(post(apiPath))
                .andExpect(status().isNotFound());
    }

    @Test
    void discardTeamToProject_ShouldReturnOk() throws Exception {
        Team team = TeamTestDataFactory.createDefaultTeam();
        when(projectService.discardTeam(project.getId(), team.getId())).thenReturn(modelMapper.map(team, TeamDto.class));

        String apiPath = BASE_PATH + "/v1/"+project.getId()+"/team/"+team.getId()+"/discard";
        mockMvc.perform(post(apiPath))
                .andExpect(status().isOk());
    }

    @Test
    void discardTeamToProject_ShouldReturnNotFound() throws Exception {
        UUID randomId = UUID.randomUUID();
        when(projectService.discardTeam(project.getId(), randomId))
                .thenThrow(new TeamNotFoundException(randomId));

        String apiPath = BASE_PATH + "/v1/"+project.getId()+"/team/"+randomId+"/discard";
        mockMvc.perform(post(apiPath))
                .andExpect(status().isNotFound());
    }

}
