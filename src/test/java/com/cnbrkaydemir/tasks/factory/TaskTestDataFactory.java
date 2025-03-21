package com.cnbrkaydemir.tasks.factory;

import com.cnbrkaydemir.tasks.dto.CreateTaskDto;
import com.cnbrkaydemir.tasks.dto.TaskDto;
import com.cnbrkaydemir.tasks.model.*;

import java.util.UUID;

public class TaskTestDataFactory {
    public static Task createDefaultTask() {
        Task task = new Task();
        task.setId(UUID.randomUUID());
        task.setName("Task 1");
        task.setDescription("Task Description 1");
        task.setProject(ProjectTestDataFactory.createDefaultProject());
        task.setProgress(TaskProgress.IN_PROGRESS);
        task.setPriority(TaskPriority.LOW);
        task.setAssignee(UsersTestDataFactory.createDefaultUser());
        return task;
    }

    public static Task createCustomTask(String name, String description, TaskProgress progress, TaskPriority priority, Users user) {
        Task task = new Task();
        task.setId(UUID.randomUUID());
        task.setName(name);
        task.setDescription(description);
        task.setProject(ProjectTestDataFactory.createDefaultProject());
        task.setAssignee(user);
        task.setProgress(progress);
        task.setPriority(priority);
        return task;
    }

    public static Task createEmptyTask(String name, String description, TaskProgress progress, TaskPriority priority) {
        Task task = new Task();
        task.setId(UUID.randomUUID());
        task.setName(name);
        task.setDescription(description);
        task.setProgress(progress);
        task.setPriority(priority);
        return task;
    }


    public static TaskDto dtoFromTask(Task task){
        TaskDto dto = new TaskDto();
        dto.setId(task.getId());
        dto.setName(task.getName());
        dto.setDescription(task.getDescription());
        dto.setProgress(task.getProgress());
        dto.setPriority(task.getPriority());
        return dto;
    }

    public static CreateTaskDto createDefaultCreateTaskDto(UUID userId, UUID projectId, TaskDto taskDto) {
        CreateTaskDto createTaskDto = new CreateTaskDto();
        createTaskDto.setId(taskDto.getId());
        createTaskDto.setName(taskDto.getName());
        createTaskDto.setDescription(taskDto.getDescription());
        createTaskDto.setUserId(userId);
        createTaskDto.setProjectId(projectId);
        return createTaskDto;
    }
}
