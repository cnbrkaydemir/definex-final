package com.cnbrkaydemir.tasks.service.impl;

import com.cnbrkaydemir.tasks.dto.DepartmentDto;
import com.cnbrkaydemir.tasks.dto.ProjectDto;
import com.cnbrkaydemir.tasks.dto.TeamDto;
import com.cnbrkaydemir.tasks.exception.notfound.DepartmentNotFoundException;
import com.cnbrkaydemir.tasks.exception.notfound.ProjectNotFoundException;
import com.cnbrkaydemir.tasks.exception.notfound.TeamNotFoundException;
import com.cnbrkaydemir.tasks.exception.state.DepartmentAlreadyContainsProjectException;
import com.cnbrkaydemir.tasks.exception.state.DepartmentAlreadyContainsTeamException;
import com.cnbrkaydemir.tasks.exception.state.DepartmentDoesNotContainProjectException;
import com.cnbrkaydemir.tasks.exception.state.DepartmentDoesNotContainTeamException;
import com.cnbrkaydemir.tasks.model.Department;
import com.cnbrkaydemir.tasks.model.Project;
import com.cnbrkaydemir.tasks.model.Team;
import com.cnbrkaydemir.tasks.repository.DepartmentRepository;
import com.cnbrkaydemir.tasks.repository.ProjectRepository;
import com.cnbrkaydemir.tasks.repository.TeamRepository;
import com.cnbrkaydemir.tasks.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = {"departments"})
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    private final ProjectRepository projectRepository;

    private final TeamRepository teamRepository;

    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(key = "#id")
    public DepartmentDto getDepartmentById(UUID id) throws DepartmentNotFoundException {
        Department targetDepartment = departmentRepository.findById(id).orElseThrow(() -> new DepartmentNotFoundException(id));
        return modelMapper.map(targetDepartment, DepartmentDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "allDepartments")
    public List<DepartmentDto> getAllDepartments() {
        return departmentRepository.findAll()
                .stream()
                .map((department)-> modelMapper.map(department, DepartmentDto.class))
                .toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = "allDepartments", allEntries = true)
    public DepartmentDto createDepartment(DepartmentDto departmentDto) {
        Department targetDepartment = modelMapper.map(departmentDto, Department.class);
        targetDepartment.setDeleted(false);
        return modelMapper.map(departmentRepository.save(targetDepartment), DepartmentDto.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @Caching(evict = {
            @CacheEvict(key = "#id"),
            @CacheEvict(cacheNames = "allDepartments", allEntries = true)
    })
    public DepartmentDto updateDepartment(UUID id, DepartmentDto department) throws DepartmentNotFoundException {
        Department oldDepartment = departmentRepository.findById(id).orElseThrow(() -> new DepartmentNotFoundException(id));
        modelMapper.map(department, oldDepartment);
        return modelMapper.map(departmentRepository.save(oldDepartment), DepartmentDto.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @Caching(evict = {
            @CacheEvict(key = "#id"),
            @CacheEvict(cacheNames = "allDepartments", allEntries = true),
            @CacheEvict(cacheNames = "departmentTeams", key = "#id"),
            @CacheEvict(cacheNames = "departmentProjects", key = "#id"),
            @CacheEvict(cacheNames = "projectDepartment", allEntries = true)
    })
    public boolean deleteDepartment(UUID id) throws DepartmentNotFoundException {
        Department targetDepartment = departmentRepository.findById(id).orElseThrow(() -> new DepartmentNotFoundException(id));
        targetDepartment.setDeleted(true);
        departmentRepository.save(targetDepartment);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "departmentTeams", key = "#id")
    public List<TeamDto> getTeamsByDepartment(UUID id) {
        Department department = departmentRepository.findById(id).orElseThrow(() -> new DepartmentNotFoundException(id));
        return departmentRepository.findActiveTeamsByDepartmentId(department.getId())
                .stream()
                .map(team -> modelMapper.map(team, TeamDto.class))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "departmentProjects", key = "#id")
    public List<ProjectDto> getProjectsByDepartment(UUID id) {
        Department department = departmentRepository.findById(id).orElseThrow(() -> new DepartmentNotFoundException(id));
        return departmentRepository.findActiveProjectsByDepartmentId(department.getId())
                .stream()
                .map(project -> modelMapper.map(project, ProjectDto.class))
                .toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @Caching(evict = {
            @CacheEvict(key = "#departmentId"),
            @CacheEvict(cacheNames = "departmentProjects", key = "#departmentId"),
            @CacheEvict(cacheNames = "allDepartments", allEntries = true),
            @CacheEvict(cacheNames = "projectDepartment", key = "#projectId")
    })
    public ProjectDto addProject(UUID departmentId, UUID projectId) throws DepartmentNotFoundException, ProjectNotFoundException {
        Department department = departmentRepository.findById(departmentId).orElseThrow(() -> new DepartmentNotFoundException(departmentId));
        Project project  = projectRepository.findById(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId));

        if (department.getProjects().contains(project)) {
            throw new DepartmentAlreadyContainsProjectException(department.getName(), project.getName());
        }

        department.getProjects().add(project);
        departmentRepository.save(department);
        return modelMapper.map(project, ProjectDto.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @Caching(evict = {
            @CacheEvict(key = "#departmentId"),
            @CacheEvict(cacheNames = "departmentProjects", key = "#departmentId"),
            @CacheEvict(cacheNames = "allDepartments", allEntries = true),
            @CacheEvict(cacheNames = "projectDepartment", key = "#projectId")
    })
    public ProjectDto discardProject(UUID departmentId, UUID projectId) throws DepartmentNotFoundException, ProjectNotFoundException {
        Department department = departmentRepository.findById(departmentId).orElseThrow(() -> new DepartmentNotFoundException(departmentId));
        Project project  = projectRepository.findById(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId));

        if (!department.getProjects().contains(project)) {
            throw new DepartmentDoesNotContainProjectException(department.getName(), project.getName());
        }

        department.getProjects().remove(project);
        departmentRepository.save(department);
        return modelMapper.map(project, ProjectDto.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @Caching(evict = {
            @CacheEvict(key = "#departmentId"),
            @CacheEvict(cacheNames = "departmentTeams", key = "#departmentId"),
            @CacheEvict(cacheNames = "allDepartments", allEntries = true)
    })
    public TeamDto addTeam(UUID departmentId, UUID teamId) throws DepartmentNotFoundException, TeamNotFoundException {
        Department department = departmentRepository.findById(departmentId).orElseThrow(() -> new DepartmentNotFoundException(departmentId));
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new TeamNotFoundException(teamId));

        if (department.getTeams().contains(team)) {
            throw new DepartmentAlreadyContainsTeamException(department.getName(), team.getName());
        }

        department.getTeams().add(team);
        departmentRepository.save(department);
        return modelMapper.map(team, TeamDto.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @Caching(evict = {
            @CacheEvict(key = "#departmentId"),
            @CacheEvict(cacheNames = "departmentTeams", key = "#departmentId"),
            @CacheEvict(cacheNames = "allDepartments", allEntries = true)
    })
    public TeamDto discardTeam(UUID departmentId, UUID teamId) throws DepartmentNotFoundException, TeamNotFoundException {
        Department department = departmentRepository.findById(departmentId).orElseThrow(() -> new DepartmentNotFoundException(departmentId));
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new TeamNotFoundException(teamId));

        if (!department.getTeams().contains(team)) {
            throw new DepartmentDoesNotContainTeamException(department.getName(), team.getName());
        }

        // There's a bug in the original code - this should be remove(), not add()
        department.getTeams().remove(team);
        departmentRepository.save(department);
        return modelMapper.map(team, TeamDto.class);
    }
}