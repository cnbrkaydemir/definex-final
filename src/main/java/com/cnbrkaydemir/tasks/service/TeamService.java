package com.cnbrkaydemir.tasks.service;

import com.cnbrkaydemir.tasks.dto.DepartmentDto;
import com.cnbrkaydemir.tasks.dto.ProjectDto;
import com.cnbrkaydemir.tasks.dto.TeamDto;
import com.cnbrkaydemir.tasks.dto.UserDto;
import com.cnbrkaydemir.tasks.exception.notfound.TeamNotFoundException;
import com.cnbrkaydemir.tasks.exception.notfound.UserNotFoundException;
import com.cnbrkaydemir.tasks.exception.state.UserAlreadyInTeamException;


import java.util.List;
import java.util.UUID;

public interface TeamService {
    TeamDto getTeamById(UUID id) throws TeamNotFoundException;
    List<TeamDto> getAllTeams();
    TeamDto createTeam(TeamDto teamDto);
    TeamDto updateTeam(UUID id, TeamDto team) throws TeamNotFoundException;
    boolean deleteTeam(UUID id) throws TeamNotFoundException;
    List<UserDto> getTeamUsers(UUID id) throws TeamNotFoundException;
    TeamDto addUserToTeam(UUID teamId, UUID userId) throws TeamNotFoundException, UserNotFoundException, UserAlreadyInTeamException;
    DepartmentDto getTeamDepartment(UUID id) throws TeamNotFoundException;
    ProjectDto getTeamProject(UUID id) throws TeamNotFoundException;
}
