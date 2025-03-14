package com.cnbrkaydemir.tasks.service;

import com.cnbrkaydemir.tasks.dto.*;
import com.cnbrkaydemir.tasks.exception.notfound.TaskNotFoundException;
import com.cnbrkaydemir.tasks.exception.notfound.TeamNotFoundException;
import com.cnbrkaydemir.tasks.exception.notfound.UserNotFoundException;
import com.cnbrkaydemir.tasks.model.TaskPriority;
import com.cnbrkaydemir.tasks.model.TaskProgress;

import java.util.List;
import java.util.UUID;

public interface TaskService {
    TaskDto getTask(UUID id) throws TaskNotFoundException;
    List<TaskDto> getTasks();
    TaskDto createTask(CreateTaskDto taskDto) throws UserNotFoundException, TeamNotFoundException;
    boolean deleteTask(UUID id) throws TaskNotFoundException;
    TaskDto updateTask(UUID id, TaskDto task) throws TaskNotFoundException;
    UserDto getTaskAssignee(UUID id) throws TaskNotFoundException;
    ProjectDto getTaskProject(UUID id) throws TaskNotFoundException;
    TaskDto updateTaskPriority(UUID id, TaskPriority priority) throws TaskNotFoundException;
    TaskDto updateTaskProgress(UUID id, TaskProgress progress) throws TaskNotFoundException;
    List<CommentDto> getTaskComments(UUID id) throws TaskNotFoundException;
    List<AttachmentDto> getTaskAttachments(UUID id) throws TaskNotFoundException;
}
