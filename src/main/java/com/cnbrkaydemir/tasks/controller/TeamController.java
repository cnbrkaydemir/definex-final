package com.cnbrkaydemir.tasks.controller;

import com.cnbrkaydemir.tasks.dto.TeamDto;
import com.cnbrkaydemir.tasks.model.Team;
import com.cnbrkaydemir.tasks.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/team")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;


    @GetMapping("/v1")
    public ResponseEntity<List<TeamDto>> list(){
        return ResponseEntity.ok(teamService.getAllTeams());
    }

    @GetMapping("/v1/{teamId}")
    public ResponseEntity<TeamDto> get(@PathVariable UUID teamId){
        return ResponseEntity.ok(teamService.getTeamById(teamId));
    }

    @PostMapping("/v1")
    public ResponseEntity<TeamDto> create(@RequestBody TeamDto teamDto){
        return ResponseEntity.created(URI.create("/api/team/v1/"+teamDto.getId()))
                .body(teamService.createTeam(teamDto));
    }

    @PatchMapping("/v1/{teamId}")
    public ResponseEntity<TeamDto> update(@PathVariable UUID teamId, @RequestBody TeamDto teamDto){
        return ResponseEntity.ok(teamService.updateTeam(teamId, teamDto));
    }

    @DeleteMapping("/v1/{teamId}")
    public ResponseEntity<Void> delete(@PathVariable UUID teamId){
        teamService.deleteTeam(teamId);
        return ResponseEntity.noContent().build();
    }
}
