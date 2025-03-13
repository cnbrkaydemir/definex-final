package com.cnbrkaydemir.tasks.service.impl;

import com.cnbrkaydemir.tasks.dto.DepartmentDto;
import com.cnbrkaydemir.tasks.dto.ProjectDto;
import com.cnbrkaydemir.tasks.dto.TeamDto;
import com.cnbrkaydemir.tasks.dto.UserDto;
import com.cnbrkaydemir.tasks.exception.notfound.TeamNotFoundException;
import com.cnbrkaydemir.tasks.exception.notfound.UserNotFoundException;
import com.cnbrkaydemir.tasks.exception.state.UserAlreadyInTeamException;
import com.cnbrkaydemir.tasks.model.Department;
import com.cnbrkaydemir.tasks.model.Project;
import com.cnbrkaydemir.tasks.model.Team;
import com.cnbrkaydemir.tasks.model.Users;
import com.cnbrkaydemir.tasks.repository.TeamRepository;
import com.cnbrkaydemir.tasks.repository.UsersRepository;
import com.cnbrkaydemir.tasks.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;

    private final UsersRepository usersRepository;

    private final ModelMapper modelMapper;

    @Override
    public TeamDto getTeamById(UUID id) throws TeamNotFoundException {
        Team team = teamRepository.findById(id).orElseThrow(() -> new TeamNotFoundException(id));
        return modelMapper.map(team, TeamDto.class);
    }

    @Override
    public List<TeamDto> getAllTeams() {
        return teamRepository.findAll()
                .stream()
                .map((team)-> modelMapper.map(team, TeamDto.class))
                .toList();
    }

    @Override
    public TeamDto createTeam(TeamDto teamDto) {
        Team team = modelMapper.map(teamDto, Team.class);
        team.setDeleted(false);
        return modelMapper.map(teamRepository.save(team), TeamDto.class);
    }

    @Override
    public TeamDto updateTeam(UUID id, TeamDto team) throws TeamNotFoundException {
        Team oldTeam = teamRepository.findById(id).orElseThrow(() -> new TeamNotFoundException(id));
        modelMapper.map(team, oldTeam);
        return modelMapper.map(teamRepository.save(oldTeam), TeamDto.class);
    }

    @Override
    public boolean deleteTeam(UUID id) throws TeamNotFoundException {
        Team team = teamRepository.findById(id).orElseThrow(() -> new TeamNotFoundException(id));
        team.setDeleted(true);
        teamRepository.save(team);
        return true;
    }

    @Override
    public List<UserDto> getTeamUsers(UUID id) throws TeamNotFoundException {
        Team team = teamRepository.findById(id).orElseThrow(()->new TeamNotFoundException(id));
        return teamRepository.findActiveUsersByTeamId(team.getId())
                .stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .toList();
    }

    @Override
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
    public DepartmentDto getTeamDepartment(UUID id) throws TeamNotFoundException {
        Team team = teamRepository.findById(id).orElseThrow(() -> new TeamNotFoundException(id));
        Department teamDepartment = teamRepository.findActiveDepartmentByTeamId(team.getId());
        return modelMapper.map(teamDepartment, DepartmentDto.class);
    }

    @Override
    public ProjectDto getTeamProject(UUID id) throws TeamNotFoundException {
        Team team = teamRepository.findById(id).orElseThrow(() -> new TeamNotFoundException(id));
        Project teamProject = teamRepository.findActiveProjectByTeamId(team.getId());
        return modelMapper.map(teamProject, ProjectDto.class);
    }
}
