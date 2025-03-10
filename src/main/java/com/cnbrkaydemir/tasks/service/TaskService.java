package com.cnbrkaydemir.tasks.service;

import com.cnbrkaydemir.tasks.dto.TaskDto;
import com.cnbrkaydemir.tasks.model.Task;

import java.util.List;
import java.util.UUID;

public interface TaskService {
    TaskDto getTask(UUID id);
    List<TaskDto> getTasks();
    TaskDto createTask(Task task);
    boolean deleteTask(UUID id);
    TaskDto updateTask(UUID id, TaskDto task);
}
