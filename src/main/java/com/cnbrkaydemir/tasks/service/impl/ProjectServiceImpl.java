package com.cnbrkaydemir.tasks.service.impl;

import com.cnbrkaydemir.tasks.dto.*;
import com.cnbrkaydemir.tasks.exception.notfound.DepartmentNotFoundException;
import com.cnbrkaydemir.tasks.exception.notfound.ProjectNotFoundException;
import com.cnbrkaydemir.tasks.exception.notfound.TeamNotFoundException;
import com.cnbrkaydemir.tasks.exception.state.ProjectAlreadyContainsTeamException;
import com.cnbrkaydemir.tasks.exception.state.ProjectDoesNotIncludeTeamException;
import com.cnbrkaydemir.tasks.model.Department;
import com.cnbrkaydemir.tasks.model.Project;
import com.cnbrkaydemir.tasks.model.Team;
import com.cnbrkaydemir.tasks.repository.DepartmentRepository;
import com.cnbrkaydemir.tasks.repository.ProjectRepository;
import com.cnbrkaydemir.tasks.repository.TeamRepository;
import com.cnbrkaydemir.tasks.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    private final DepartmentRepository departmentRepository;

    private final TeamRepository teamRepository;

    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public ProjectDto getProject(UUID id) throws ProjectNotFoundException {
        Project project = projectRepository.findById(id).orElseThrow(() -> new ProjectNotFoundException(id));
        return modelMapper.map(project, ProjectDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectDto> getProjects() {
        return projectRepository.findAll()
                .stream()
                .map((project -> modelMapper.map(project, ProjectDto.class)))
                .toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProjectDto createProject(CreateProjectDto projectDto) {
        Project project = modelMapper.map(projectDto, Project.class);
        Department department = departmentRepository.findById(projectDto.getDepartmentId())
                .orElseThrow(()-> new DepartmentNotFoundException(projectDto.getDepartmentId()));
        project.setDepartment(department);
        project.setDeleted(false);
        return modelMapper.map(projectRepository.save(project), ProjectDto.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteProject(UUID id) throws ProjectNotFoundException {
        Project project = projectRepository.findById(id).orElseThrow(() -> new ProjectNotFoundException(id));
        project.setDeleted(true);
        projectRepository.save(project);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProjectDto updateProject(UUID id, ProjectDto project) throws ProjectNotFoundException {
        Project updatedProject = projectRepository.findById(id).orElseThrow(() -> new ProjectNotFoundException(id));
        modelMapper.map(project, updatedProject);
        return modelMapper.map(projectRepository.save(updatedProject), ProjectDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentDto getDepartment(UUID id) throws ProjectNotFoundException {
        Project project = projectRepository.findById(id).orElseThrow(() -> new ProjectNotFoundException(id));
        Department department = projectRepository.findActiveDepartmentByProjectId(project.getId());
        return modelMapper.map(department, DepartmentDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeamDto> getTeams(UUID id) throws ProjectNotFoundException {
        Project project = projectRepository.findById(id).orElseThrow(() -> new ProjectNotFoundException(id));
        return projectRepository.findActiveTeamsByProjectId(project.getId())
                .stream()
                .map((team)->  modelMapper.map(team, TeamDto.class))
                .toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TeamDto addTeam(UUID projectId, UUID teamID) throws ProjectNotFoundException, TeamNotFoundException {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId));
        Team team = teamRepository.findById(teamID).orElseThrow(() -> new TeamNotFoundException(teamID));

        if(project.getTeams().contains(team)) {
            throw new ProjectAlreadyContainsTeamException(project.getName(), team.getName());
        }

        project.getTeams().add(team);
        projectRepository.save(project);
        return modelMapper.map(team, TeamDto.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TeamDto discardTeam(UUID projectId, UUID teamID) throws ProjectNotFoundException, TeamNotFoundException, ProjectDoesNotIncludeTeamException {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId));
        Team team = teamRepository.findById(teamID).orElseThrow(() -> new TeamNotFoundException(teamID));

        if (!project.getTeams().contains(team)) {
            throw new ProjectDoesNotIncludeTeamException();
        }

        project.getTeams().remove(team);
        return modelMapper.map(team, TeamDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskDto> getTasks(UUID id) throws ProjectNotFoundException {
        Project project = projectRepository.findById(id).orElseThrow(() -> new ProjectNotFoundException(id));
        return projectRepository.findActiveTasksByProjectId(project.getId())
                .stream()
                .map(task -> modelMapper.map(task, TaskDto.class))
                .toList();
    }
}
