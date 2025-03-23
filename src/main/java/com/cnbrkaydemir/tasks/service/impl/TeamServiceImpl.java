package com.cnbrkaydemir.tasks.service.impl;

import com.cnbrkaydemir.tasks.dto.*;
import com.cnbrkaydemir.tasks.exception.notfound.DepartmentNotFoundException;
import com.cnbrkaydemir.tasks.exception.notfound.TeamNotFoundException;
import com.cnbrkaydemir.tasks.exception.notfound.UserNotFoundException;
import com.cnbrkaydemir.tasks.exception.state.UserAlreadyInTeamException;
import com.cnbrkaydemir.tasks.exception.state.UserNotInTeamException;
import com.cnbrkaydemir.tasks.model.Department;
import com.cnbrkaydemir.tasks.model.Project;
import com.cnbrkaydemir.tasks.model.Team;
import com.cnbrkaydemir.tasks.model.Users;
import com.cnbrkaydemir.tasks.repository.DepartmentRepository;
import com.cnbrkaydemir.tasks.repository.TeamRepository;
import com.cnbrkaydemir.tasks.repository.UsersRepository;
import com.cnbrkaydemir.tasks.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "teams")
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;

    private final UsersRepository usersRepository;

    private final DepartmentRepository departmentRepository;

    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "team", key = "#id")
    public TeamDto getTeamById(UUID id) throws TeamNotFoundException {
        Team team = teamRepository.findById(id).orElseThrow(() -> new TeamNotFoundException(id));
        return modelMapper.map(team, TeamDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "teams")
    public List<TeamDto> getAllTeams() {
        return teamRepository.findAll()
                .stream()
                .map((team)-> modelMapper.map(team, TeamDto.class))
                .toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CachePut(value = "team", key = "#result.id")
    @CacheEvict(value = "teams", allEntries = true)
    public TeamDto createTeam(CreateTeamDto teamDto) {
        Department department = departmentRepository.findById(teamDto.getDepartment()).orElseThrow(() -> new DepartmentNotFoundException(teamDto.getDepartment()));
        Team team = modelMapper.map(teamDto, Team.class);
        department.getTeams().add(team);
        team.setDeleted(false);
        return modelMapper.map(teamRepository.save(team), TeamDto.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CachePut(key = "#id", value = "team")
    @CacheEvict(value = "teams", allEntries = true)
    public TeamDto updateTeam(UUID id, TeamDto team) throws TeamNotFoundException {
        Team oldTeam = teamRepository.findById(id).orElseThrow(() -> new TeamNotFoundException(id));
        modelMapper.map(team, oldTeam);
        return modelMapper.map(teamRepository.save(oldTeam), TeamDto.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "teams", allEntries = true)
    public boolean deleteTeam(UUID id) throws TeamNotFoundException {
        Team team = teamRepository.findById(id).orElseThrow(() -> new TeamNotFoundException(id));
        team.setDeleted(true);
        teamRepository.save(team);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "users")
    public List<UserDto> getTeamUsers(UUID id) throws TeamNotFoundException {
        Team team = teamRepository.findById(id).orElseThrow(()->new TeamNotFoundException(id));
        return teamRepository.findActiveUsersByTeamId(team.getId())
                .stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CachePut(value = "team", key = "#teamId")
    @CacheEvict(cacheNames = {"teams", "users"}, allEntries = true)
    public TeamDto addUserToTeam(UUID teamId, UUID userId) throws TeamNotFoundException, UserNotFoundException, UserAlreadyInTeamException {
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new TeamNotFoundException(teamId));
        Users user = usersRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        if(team.getTeamMembers().contains(user)) {
            throw new UserAlreadyInTeamException(userId, teamId);
        }

        team.getTeamMembers().add(user);
        return modelMapper.map(teamRepository.save(team), TeamDto.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CachePut(value = "team", key = "#teamId")
    @CacheEvict(cacheNames = {"teams", "users"}, allEntries = true)
    public TeamDto discardUserFromTeam(UUID teamId, UUID userId) throws TeamNotFoundException, UserNotFoundException, UserNotInTeamException {
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new TeamNotFoundException(teamId));
        Users user = usersRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        if(!team.getTeamMembers().contains(user)) {
            throw new UserNotInTeamException(user, team);
        }

        team.getTeamMembers().remove(user);
        return modelMapper.map(teamRepository.save(team), TeamDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "department")
    public DepartmentDto getTeamDepartment(UUID id) throws TeamNotFoundException {
        Team team = teamRepository.findById(id).orElseThrow(() -> new TeamNotFoundException(id));
        Department teamDepartment = teamRepository.findActiveDepartmentByTeamId(team.getId());
        return modelMapper.map(teamDepartment, DepartmentDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "project")
    public ProjectDto getTeamProject(UUID id) throws TeamNotFoundException {
        Team team = teamRepository.findById(id).orElseThrow(() -> new TeamNotFoundException(id));
        Project teamProject = teamRepository.findActiveProjectByTeamId(team.getId());
        return modelMapper.map(teamProject, ProjectDto.class);
    }
}
