package com.cnbrkaydemir.tasks.service.impl;

import com.cnbrkaydemir.tasks.dto.TeamDto;
import com.cnbrkaydemir.tasks.exception.notfound.TeamNotFoundException;
import com.cnbrkaydemir.tasks.model.Team;
import com.cnbrkaydemir.tasks.repository.TeamRepository;
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
}
