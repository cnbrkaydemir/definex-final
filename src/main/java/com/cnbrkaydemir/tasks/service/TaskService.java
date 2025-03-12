package com.cnbrkaydemir.tasks.service;

import com.cnbrkaydemir.tasks.dto.TaskDto;
import com.cnbrkaydemir.tasks.exception.notfound.TaskNotFoundException;
import com.cnbrkaydemir.tasks.model.Task;

import java.util.List;
import java.util.UUID;

public interface TaskService {
    TaskDto getTask(UUID id) throws TaskNotFoundException;
    List<TaskDto> getTasks();
    TaskDto createTask(Task task);
    boolean deleteTask(UUID id) throws TaskNotFoundException;
    TaskDto updateTask(UUID id, TaskDto task) throws TaskNotFoundException;
}
