package com.cnbrkaydemir.tasks.exception.notfound;

import java.util.UUID;

public class TaskNotFoundException extends NotFoundException {
    public TaskNotFoundException(UUID taskId) {
        super("Task with id " + taskId + " not found");
    }
}
