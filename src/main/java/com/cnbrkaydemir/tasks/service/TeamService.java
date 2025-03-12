package com.cnbrkaydemir.tasks.service;

import com.cnbrkaydemir.tasks.dto.TeamDto;
import com.cnbrkaydemir.tasks.exception.notfound.TeamNotFoundException;
import com.cnbrkaydemir.tasks.model.Team;

import java.util.List;
import java.util.UUID;

public interface TeamService {
    TeamDto getTeamById(UUID id) throws TeamNotFoundException;
    List<TeamDto> getAllTeams();
    TeamDto createTeam(Team team);
    TeamDto updateTeam(UUID id, TeamDto team) throws TeamNotFoundException;
    boolean deleteTeam(UUID id) throws TeamNotFoundException;
}
