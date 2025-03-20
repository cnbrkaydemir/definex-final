package com.cnbrkaydemir.tasks.controller;

import com.cnbrkaydemir.tasks.dto.CreateProjectDto;
import com.cnbrkaydemir.tasks.dto.ProjectDto;
import com.cnbrkaydemir.tasks.exception.GlobalExceptionHandler;
import com.cnbrkaydemir.tasks.exception.notfound.ProjectNotFoundException;
import com.cnbrkaydemir.tasks.factory.ProjectTestDataFactory;
import com.cnbrkaydemir.tasks.model.Project;
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


}
