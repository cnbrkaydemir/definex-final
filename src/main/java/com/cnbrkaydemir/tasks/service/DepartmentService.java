package com.cnbrkaydemir.tasks.service;

import com.cnbrkaydemir.tasks.dto.DepartmentDto;
import com.cnbrkaydemir.tasks.dto.ProjectDto;
import com.cnbrkaydemir.tasks.dto.TeamDto;
import com.cnbrkaydemir.tasks.exception.notfound.DepartmentNotFoundException;
import com.cnbrkaydemir.tasks.exception.notfound.ProjectNotFoundException;
import com.cnbrkaydemir.tasks.exception.notfound.TeamNotFoundException;
import com.cnbrkaydemir.tasks.model.Department;

import java.util.List;
import java.util.UUID;

public interface DepartmentService {
    DepartmentDto getDepartmentById(UUID id) throws DepartmentNotFoundException;
    List<DepartmentDto> getAllDepartments();
    DepartmentDto createDepartment(DepartmentDto departmentDto);
    DepartmentDto updateDepartment(UUID id, DepartmentDto department) throws DepartmentNotFoundException;
    boolean deleteDepartment(UUID id) throws DepartmentNotFoundException;
    List<TeamDto> getTeamsByDepartment(UUID id);
    List<ProjectDto> getProjectsByDepartment(UUID id);
    ProjectDto addProject(UUID departmentId, UUID projectId) throws DepartmentNotFoundException, ProjectNotFoundException;
    ProjectDto discardProject(UUID departmentId, UUID projectId) throws DepartmentNotFoundException, ProjectNotFoundException;
    TeamDto addTeam(UUID departmentId, UUID teamId) throws DepartmentNotFoundException, TeamNotFoundException;
    TeamDto discardTeam(UUID departmentId, UUID teamId) throws DepartmentNotFoundException, TeamNotFoundException;
}
