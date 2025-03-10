package com.cnbrkaydemir.tasks.service;

import com.cnbrkaydemir.tasks.dto.ProjectDto;
import com.cnbrkaydemir.tasks.model.Project;

import java.util.List;
import java.util.UUID;

public interface ProjectService {
    ProjectDto getProject(UUID id);
    List<ProjectDto> getProjects();
    ProjectDto saveProject(Project project);
    boolean deleteProject(UUID id);
    ProjectDto updateProject(UUID id, ProjectDto project);
}
