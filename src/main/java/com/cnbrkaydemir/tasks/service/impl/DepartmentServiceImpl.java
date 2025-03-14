package com.cnbrkaydemir.tasks.service.impl;

import com.cnbrkaydemir.tasks.dto.DepartmentDto;
import com.cnbrkaydemir.tasks.dto.ProjectDto;
import com.cnbrkaydemir.tasks.dto.TeamDto;
import com.cnbrkaydemir.tasks.exception.notfound.DepartmentNotFoundException;
import com.cnbrkaydemir.tasks.exception.notfound.ProjectNotFoundException;
import com.cnbrkaydemir.tasks.exception.notfound.TeamNotFoundException;
import com.cnbrkaydemir.tasks.model.Department;
import com.cnbrkaydemir.tasks.model.Project;
import com.cnbrkaydemir.tasks.model.Team;
import com.cnbrkaydemir.tasks.repository.DepartmentRepository;
import com.cnbrkaydemir.tasks.repository.ProjectRepository;
import com.cnbrkaydemir.tasks.repository.TeamRepository;
import com.cnbrkaydemir.tasks.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    private final ProjectRepository projectRepository;

    private final TeamRepository teamRepository;

    private final ModelMapper modelMapper;

    @Override
    public DepartmentDto getDepartmentById(UUID id) throws DepartmentNotFoundException {
        Department targetDepartment = departmentRepository.findById(id).orElseThrow(() -> new DepartmentNotFoundException(id));
        return modelMapper.map(targetDepartment, DepartmentDto.class);
    }

    @Override
    public List<DepartmentDto> getAllDepartments() {
        return departmentRepository.findAll()
                .stream()
                .map((department)-> modelMapper.map(department, DepartmentDto.class))
                .toList();
    }

    @Override
    public DepartmentDto createDepartment(DepartmentDto departmentDto) {
        Department targetDepartment = modelMapper.map(departmentDto, Department.class);
        targetDepartment.setDeleted(false);
        return modelMapper.map(departmentRepository.save(targetDepartment), DepartmentDto.class);
    }

    @Override
    public DepartmentDto updateDepartment(UUID id, DepartmentDto department) throws DepartmentNotFoundException {
        Department oldDepartment = departmentRepository.findById(id).orElseThrow(() -> new DepartmentNotFoundException(id));
        modelMapper.map(department, oldDepartment);
        return modelMapper.map(departmentRepository.save(oldDepartment), DepartmentDto.class);
    }

    @Override
    public boolean deleteDepartment(UUID id) throws DepartmentNotFoundException {
        Department targetDepartment = departmentRepository.findById(id).orElseThrow(() -> new DepartmentNotFoundException(id));
        targetDepartment.setDeleted(true);
        departmentRepository.save(targetDepartment);
        return true;
    }

    @Override
    public List<TeamDto> getTeamsByDepartment(UUID id) {
        Department department = departmentRepository.findById(id).orElseThrow(() -> new DepartmentNotFoundException(id));
        return departmentRepository.findActiveTeamsByDepartmentId(department.getId())
                .stream()
                .map(team -> modelMapper.map(team, TeamDto.class))
                .toList();
    }

    @Override
    public List<ProjectDto> getProjectsByDepartment(UUID id) {
        Department department = departmentRepository.findById(id).orElseThrow(() -> new DepartmentNotFoundException(id));
        return departmentRepository.findActiveProjectsByDepartmentId(department.getId())
                .stream()
                .map(project -> modelMapper.map(project, ProjectDto.class))
                .toList();
    }

    @Override
    public ProjectDto addProject(UUID departmentId, UUID projectId) throws DepartmentNotFoundException, ProjectNotFoundException {
        Department department = departmentRepository.findById(departmentId).orElseThrow(() -> new DepartmentNotFoundException(departmentId));
        Project project  = projectRepository.findById(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId));
        department.getProjects().add(project);
        return modelMapper.map(project, ProjectDto.class);
    }

    @Override
    public ProjectDto discardProject(UUID departmentId, UUID projectId) throws DepartmentNotFoundException, ProjectNotFoundException {
        Department department = departmentRepository.findById(departmentId).orElseThrow(() -> new DepartmentNotFoundException(departmentId));
        Project project  = projectRepository.findById(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId));
        department.getProjects().remove(project);
        return modelMapper.map(project, ProjectDto.class);
    }

    @Override
    public TeamDto addTeam(UUID departmentId, UUID teamId) throws DepartmentNotFoundException, TeamNotFoundException {
        Department department = departmentRepository.findById(departmentId).orElseThrow(() -> new DepartmentNotFoundException(departmentId));
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new TeamNotFoundException(teamId));
        department.getTeams().add(team);
        return modelMapper.map(team, TeamDto.class);
    }

    @Override
    public TeamDto discardTeam(UUID departmentId, UUID teamId) throws DepartmentNotFoundException, TeamNotFoundException {
        Department department = departmentRepository.findById(departmentId).orElseThrow(() -> new DepartmentNotFoundException(departmentId));
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new TeamNotFoundException(teamId));
        department.getTeams().add(team);
        return modelMapper.map(team, TeamDto.class);
    }
}
