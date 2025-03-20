package com.cnbrkaydemir.tasks.controller;

import com.cnbrkaydemir.tasks.dto.DepartmentDto;
import com.cnbrkaydemir.tasks.dto.ProjectDto;
import com.cnbrkaydemir.tasks.dto.TeamDto;
import com.cnbrkaydemir.tasks.exception.GlobalExceptionHandler;
import com.cnbrkaydemir.tasks.exception.notfound.DepartmentNotFoundException;
import com.cnbrkaydemir.tasks.exception.notfound.ProjectNotFoundException;
import com.cnbrkaydemir.tasks.exception.notfound.TeamNotFoundException;
import com.cnbrkaydemir.tasks.factory.DepartmentTestDataFactory;
import com.cnbrkaydemir.tasks.factory.ProjectTestDataFactory;
import com.cnbrkaydemir.tasks.factory.TeamTestDataFactory;
import com.cnbrkaydemir.tasks.model.Department;
import com.cnbrkaydemir.tasks.model.Project;
import com.cnbrkaydemir.tasks.model.Team;
import com.cnbrkaydemir.tasks.service.DepartmentService;
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
import java.util.stream.Stream;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DepartmentControllerTest {

    private final String BASE_PATH = "/api/department";

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private DepartmentController departmentController;

    @Mock
    private DepartmentService departmentService;


    private ObjectMapper objectMapper;

    private ModelMapper modelMapper;

    private Department department;

    private DepartmentDto departmentDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(departmentController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        modelMapper = new ModelMapper();

        department = DepartmentTestDataFactory.createCustomDepartment("Application Development", "Developing Applications....");
        departmentDto = modelMapper.map(department, DepartmentDto.class);

        objectMapper = new ObjectMapper();
    }

    @Test
    void findAll_ShouldReturnOk() throws Exception {
        when(departmentService.getAllDepartments()).thenReturn(List.of(departmentDto));

        String apiPath = BASE_PATH + "/v1";
        mockMvc.perform(get(apiPath))
                .andExpect(status().isOk());
    }

    @Test
    void findById_ShouldReturnOk() throws Exception {
        when(departmentService.getDepartmentById(department.getId())).thenReturn(departmentDto);

        String apiPath = BASE_PATH + "/v1/"+department.getId();
        mockMvc.perform(get(apiPath))
                .andExpect(status().isOk());
    }

    @Test
    void findById_ShouldReturnNotFound() throws Exception {
        UUID randomId = UUID.randomUUID();
        when(departmentService.getDepartmentById(randomId))
                .thenThrow(new DepartmentNotFoundException(randomId));

        String apiPath = BASE_PATH + "/v1/" + randomId.toString();
        mockMvc.perform(get(apiPath))
                .andExpect(status().isNotFound());
    }

    @Test
    void createDepartment_ShouldReturnCreated() throws Exception {
        when(departmentService.createDepartment(departmentDto)).thenReturn(departmentDto);

        String apiPath = BASE_PATH + "/v1";
        mockMvc.perform(post(apiPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(departmentDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void updateDepartment_ShouldReturnOk() throws Exception {
        departmentDto.setId(UUID.randomUUID());
        departmentDto.setDescription("Updated Description");
        when(departmentService.updateDepartment(departmentDto.getId(), departmentDto)).thenReturn(departmentDto);

        String apiPath = BASE_PATH + "/v1/"+departmentDto.getId();
        mockMvc.perform(patch(apiPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(departmentDto)))
                .andExpect(status().isOk());
    }

    @Test
    void updateDepartment_ShouldReturnNotFoundt() throws Exception {
        UUID randomId = UUID.randomUUID();
        when(departmentService.updateDepartment(randomId, departmentDto))
                .thenThrow(new DepartmentNotFoundException(randomId));

        String apiPath = BASE_PATH + "/v1/"+randomId.toString();
        mockMvc.perform(patch(apiPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(departmentDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteDepartment_ShouldReturnNoContent() throws Exception {
        when(departmentService.deleteDepartment(department.getId())).thenReturn(true);

        String apiPath = BASE_PATH + "/v1/"+department.getId();
        mockMvc.perform(delete(apiPath))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteDepartment_ShouldReturnNotFound() throws Exception {
        UUID randomId = UUID.randomUUID();
        when(departmentService.deleteDepartment(randomId))
                .thenThrow(new DepartmentNotFoundException(randomId));

        String apiPath = BASE_PATH + "/v1/"+randomId.toString();
        mockMvc.perform(delete(apiPath))
                .andExpect(status().isNotFound());
    }

    @Test
    void findDepartmentProjects_ShouldReturnOk() throws Exception {
        Project projects = ProjectTestDataFactory.createDefaultProject();
        when(departmentService.getProjectsByDepartment(department.getId()))
                .thenReturn(Stream.of(projects)
                                .map(project -> modelMapper.map(project, ProjectDto.class))
                                .toList());

        String apiPath = BASE_PATH + "/v1/"+department.getId()+"/projects";
        mockMvc.perform(get(apiPath))
                .andExpect(status().isOk());
    }

    @Test
    void findDepartmentProjects_ShouldReturnNotFound() throws Exception {
        UUID randomId = UUID.randomUUID();
        when(departmentService.getProjectsByDepartment(randomId))
                .thenThrow(new DepartmentNotFoundException(randomId));

        String apiPath = BASE_PATH + "/v1/"+randomId+"/projects";
        mockMvc.perform(get(apiPath))
                .andExpect(status().isNotFound());
    }

    @Test
    void findDepartmentTeams_ShouldReturnOk() throws Exception {
        Team teams = TeamTestDataFactory.createDefaultTeam();
        when(departmentService.getTeamsByDepartment(department.getId()))
                .thenReturn(Stream.of(teams)
                        .map(team -> modelMapper.map(team, TeamDto.class))
                        .toList());

        String apiPath = BASE_PATH + "/v1/"+department.getId()+"/teams";
        mockMvc.perform(get(apiPath))
                .andExpect(status().isOk());
    }

    @Test
    void findDepartmentTeams_ShouldReturnNotFound() throws Exception {
        UUID randomId = UUID.randomUUID();
        when(departmentService.getTeamsByDepartment(randomId))
                .thenThrow(new DepartmentNotFoundException(randomId));

        String apiPath = BASE_PATH + "/v1/"+randomId+"/teams";
        mockMvc.perform(get(apiPath))
                .andExpect(status().isNotFound());
    }

    @Test
    void addTeamToDepartment_ShouldReturnOk() throws Exception {
        Team team = TeamTestDataFactory.createDefaultTeam();
        when(departmentService.addTeam(department.getId(), team.getId())).thenReturn(modelMapper.map(team, TeamDto.class));

        String apiPath = BASE_PATH + "/v1/"+department.getId()+"/team/"+team.getId()+"/add";
        mockMvc.perform(post(apiPath))
                .andExpect(status().isOk());
    }

    @Test
    void addTeamToDepartment_ShouldReturnNotFound() throws Exception {
        UUID randomId = UUID.randomUUID();
        when(departmentService.addTeam(department.getId(), randomId))
                .thenThrow(new TeamNotFoundException(randomId));

        String apiPath = BASE_PATH + "/v1/"+department.getId()+"/team/"+randomId+"/add";
        mockMvc.perform(post(apiPath))
                .andExpect(status().isNotFound());
    }

    @Test
    void discardTeamToDepartment_ShouldReturnOk() throws Exception {
        Team team = TeamTestDataFactory.createDefaultTeam();
        when(departmentService.discardTeam(department.getId(), team.getId())).thenReturn(modelMapper.map(team, TeamDto.class));

        String apiPath = BASE_PATH + "/v1/"+department.getId()+"/team/"+team.getId()+"/discard";
        mockMvc.perform(post(apiPath))
                .andExpect(status().isOk());
    }

    @Test
    void discardTeamToDepartment_ShouldReturnNotFound() throws Exception {
        UUID randomId = UUID.randomUUID();
        when(departmentService.discardTeam(department.getId(), randomId))
                .thenThrow(new TeamNotFoundException(randomId));

        String apiPath = BASE_PATH + "/v1/"+department.getId()+"/team/"+randomId+"/discard";
        mockMvc.perform(post(apiPath))
                .andExpect(status().isNotFound());
    }

    @Test
    void addProjectToDepartment_ShouldReturnOk() throws Exception {
        Project project = ProjectTestDataFactory.createDefaultProject();
        when(departmentService.addProject(project.getId(), project.getId())).thenReturn(modelMapper.map(project, ProjectDto.class));

        String apiPath = BASE_PATH + "/v1/"+project.getId()+"/project/"+project.getId()+"/add";
        mockMvc.perform(post(apiPath))
                .andExpect(status().isOk());
    }

    @Test
    void addProjectToDepartment_ShouldReturnNotFound() throws Exception {
        UUID randomId = UUID.randomUUID();
        when(departmentService.addProject(department.getId(), randomId))
                .thenThrow(new ProjectNotFoundException(randomId));

        String apiPath = BASE_PATH + "/v1/"+department.getId()+"/project/"+randomId+"/add";
        mockMvc.perform(post(apiPath))
                .andExpect(status().isNotFound());
    }

    @Test
    void discardProjectToDepartment_ShouldReturnOk() throws Exception {
        Project project = ProjectTestDataFactory.createDefaultProject();
        when(departmentService.discardProject(project.getId(), project.getId()))
                .thenReturn(modelMapper.map(project, ProjectDto.class));

        String apiPath = BASE_PATH + "/v1/"+project.getId()+"/project/"+project.getId()+"/discard";
        mockMvc.perform(post(apiPath))
                .andExpect(status().isOk());
    }

    @Test
    void discardProjectToDepartment_ShouldReturnNotFound() throws Exception {
        UUID randomId = UUID.randomUUID();
        when(departmentService.discardProject(department.getId(), randomId))
                .thenThrow(new ProjectNotFoundException(randomId));

        String apiPath = BASE_PATH + "/v1/"+department.getId()+"/project/"+randomId+"/discard";
        mockMvc.perform(post(apiPath))
                .andExpect(status().isNotFound());
    }


}
