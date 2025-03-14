package com.cnbrkaydemir.tasks.service;

import com.cnbrkaydemir.tasks.dto.*;
import com.cnbrkaydemir.tasks.exception.notfound.ProjectNotFoundException;
import com.cnbrkaydemir.tasks.exception.notfound.TeamNotFoundException;
import com.cnbrkaydemir.tasks.exception.state.ProjectDoesNotIncludeTeamException;


import java.util.List;
import java.util.UUID;

public interface ProjectService {
    ProjectDto getProject(UUID id) throws ProjectNotFoundException;
    List<ProjectDto> getProjects();
    ProjectDto createProject(CreateProjectDto projectDto);
    boolean deleteProject(UUID id) throws ProjectNotFoundException;
    ProjectDto updateProject(UUID id, ProjectDto project) throws ProjectNotFoundException;
    DepartmentDto getDepartment(UUID id) throws ProjectNotFoundException;
    List<TeamDto> getTeams(UUID id) throws ProjectNotFoundException;
    TeamDto addTeam(UUID projectId, UUID teamID) throws ProjectNotFoundException, TeamNotFoundException;
    TeamDto discardTeam(UUID projectId, UUID teamID) throws ProjectNotFoundException, TeamNotFoundException, ProjectDoesNotIncludeTeamException;
    List<TaskDto> getTasks(UUID id) throws ProjectNotFoundException;
}
