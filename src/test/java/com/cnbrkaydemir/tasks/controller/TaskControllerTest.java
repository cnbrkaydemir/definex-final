package com.cnbrkaydemir.tasks.controller;

import com.cnbrkaydemir.tasks.dto.CreateTaskDto;
import com.cnbrkaydemir.tasks.dto.TaskDto;
import com.cnbrkaydemir.tasks.exception.GlobalExceptionHandler;
import com.cnbrkaydemir.tasks.exception.notfound.TaskNotFoundException;
import com.cnbrkaydemir.tasks.exception.notfound.UserNotFoundException;
import com.cnbrkaydemir.tasks.factory.TaskTestDataFactory;
import com.cnbrkaydemir.tasks.model.Task;
import com.cnbrkaydemir.tasks.service.TaskService;
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

public class TaskControllerTest {
    private final String BASE_PATH = "/api/task";

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private TaskController taskController;

    @Mock
    private TaskService taskService;

    private ObjectMapper objectMapper;

    private ModelMapper modelMapper;

    private Task task;

    private TaskDto taskDto;

    private CreateTaskDto createTaskDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(taskController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        modelMapper = new ModelMapper();

        task = TaskTestDataFactory.createDefaultTask();
        taskDto = modelMapper.map(task, TaskDto.class);

        createTaskDto = modelMapper.map(task, CreateTaskDto.class);
        createTaskDto.setProjectId(task.getProject().getId());
        createTaskDto.setUserId(task.getAssignee().getId());


        objectMapper = new ObjectMapper();
    }

    @Test
    void findAllTasks_ShouldReturnOk() throws Exception {
        when(taskService.getTasks()).thenReturn(List.of(taskDto));

        String apiPath = BASE_PATH + "/v1";
        mockMvc.perform(get(apiPath))
                .andExpect(status().isOk());
    }

    @Test
    void findTaskById_ShouldReturnOk() throws Exception {
        when(taskService.getTask(task.getId())).thenReturn(taskDto);

        String apiPath = BASE_PATH + "/v1/"+ task.getId();
        mockMvc.perform(get(apiPath))
                .andExpect(status().isOk());
    }

    @Test
    void findTaskById_ShouldReturnNotFound() throws Exception {
        UUID randomId = UUID.randomUUID();
        when(taskService.getTask(randomId))
                .thenThrow(new UserNotFoundException(randomId));

        String apiPath = BASE_PATH + "/v1/" + randomId.toString();
        mockMvc.perform(get(apiPath))
                .andExpect(status().isNotFound());
    }

    @Test
    void createTask_ShouldReturnCreated() throws Exception {
        when(taskService.createTask(createTaskDto)).thenReturn(taskDto);

        String apiPath = BASE_PATH + "/v1";
        mockMvc.perform(post(apiPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createTaskDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void updateTask_ShouldReturnOk() throws Exception {
        taskDto.setId(UUID.randomUUID());
        taskDto.setDescription("Defect Solution");
        when(taskService.updateTask(taskDto.getId(), taskDto)).thenReturn(taskDto);

        String apiPath = BASE_PATH + "/v1/"+ taskDto.getId();
        mockMvc.perform(patch(apiPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().isOk());
    }

    @Test
    void updateTask_ShouldReturnNotFound() throws Exception {
        UUID randomId = UUID.randomUUID();
        when(taskService.updateTask(randomId, taskDto))
                .thenThrow(new TaskNotFoundException(randomId));

        String apiPath = BASE_PATH + "/v1/"+ randomId.toString();
        mockMvc.perform(patch(apiPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteTask_ShouldReturnNoContent() throws Exception {
        when(taskService.deleteTask(task.getId())).thenReturn(true);

        String apiPath = BASE_PATH + "/v1/"+ task.getId();
        mockMvc.perform(delete(apiPath))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteUser_ShouldReturnNotFound() throws Exception {
        UUID randomId = UUID.randomUUID();
        when(taskService.deleteTask(randomId))
                .thenThrow(new TaskNotFoundException(randomId));

        String apiPath = BASE_PATH + "/v1/"+randomId;
        mockMvc.perform(delete(apiPath))
                .andExpect(status().isNotFound());
    }


}
