package com.cnbrkaydemir.tasks.factory;

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
}
