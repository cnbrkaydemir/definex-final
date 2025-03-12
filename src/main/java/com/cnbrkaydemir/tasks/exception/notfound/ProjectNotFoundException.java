package com.cnbrkaydemir.tasks.exception.notfound;

import java.util.UUID;

public class ProjectNotFoundException extends NotFoundException {
    public ProjectNotFoundException(UUID projectId) {
        super("Project with id " + projectId + " not found");
    }
}
