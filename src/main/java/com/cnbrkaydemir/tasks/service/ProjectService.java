package com.cnbrkaydemir.tasks.service;

import com.cnbrkaydemir.tasks.dto.ProjectDto;
import com.cnbrkaydemir.tasks.exception.notfound.ProjectNotFoundException;
import com.cnbrkaydemir.tasks.model.Project;

import java.util.List;
import java.util.UUID;

public interface ProjectService {
    ProjectDto getProject(UUID id) throws ProjectNotFoundException;
    List<ProjectDto> getProjects();
    ProjectDto createProject(ProjectDto projectDto);
    boolean deleteProject(UUID id) throws ProjectNotFoundException;
    ProjectDto updateProject(UUID id, ProjectDto project) throws ProjectNotFoundException;
}
