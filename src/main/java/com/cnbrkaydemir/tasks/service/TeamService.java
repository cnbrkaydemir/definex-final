package com.cnbrkaydemir.tasks.service;

import com.cnbrkaydemir.tasks.dto.*;
import com.cnbrkaydemir.tasks.exception.notfound.TeamNotFoundException;
import com.cnbrkaydemir.tasks.exception.notfound.UserNotFoundException;
import com.cnbrkaydemir.tasks.exception.state.UserAlreadyInTeamException;
import com.cnbrkaydemir.tasks.exception.state.UserNotInTeamException;


import java.util.List;
import java.util.UUID;

public interface TeamService {
    TeamDto getTeamById(UUID id) throws TeamNotFoundException;
    List<TeamDto> getAllTeams();
    TeamDto createTeam(CreateTeamDto teamDto);
    TeamDto updateTeam(UUID id, TeamDto team) throws TeamNotFoundException;
    boolean deleteTeam(UUID id) throws TeamNotFoundException;
    List<UserDto> getTeamUsers(UUID id) throws TeamNotFoundException;
    TeamDto addUserToTeam(UUID teamId, UUID userId) throws TeamNotFoundException, UserNotFoundException, UserAlreadyInTeamException;
    TeamDto discardUserFromTeam(UUID teamId, UUID userId) throws TeamNotFoundException, UserNotFoundException, UserNotInTeamException;
    DepartmentDto getTeamDepartment(UUID id) throws TeamNotFoundException;
    ProjectDto getTeamProject(UUID id) throws TeamNotFoundException;
}
