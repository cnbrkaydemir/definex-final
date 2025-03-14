package com.cnbrkaydemir.tasks.controller;

import com.cnbrkaydemir.tasks.dto.*;
import com.cnbrkaydemir.tasks.model.Project;
import com.cnbrkaydemir.tasks.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("/v1")
    public ResponseEntity<List<ProjectDto>> list(){
        return ResponseEntity.ok(projectService.getProjects());
    }

    @GetMapping("/v1/{projectId}")
    public ResponseEntity<ProjectDto> get(@PathVariable UUID projectId){
        return ResponseEntity.ok(projectService.getProject(projectId));
    }

    @PostMapping("/v1")
    public ResponseEntity<ProjectDto> create(@RequestBody CreateProjectDto projectDto){
        return ResponseEntity.created(URI.create("/api/project/" + projectDto.getId()))
                .body(projectService.createProject(projectDto));
    }

    @PatchMapping("/v1/{projectId}")
    public ResponseEntity<ProjectDto> update(@PathVariable UUID projectId, @RequestBody ProjectDto projectDto){
        return ResponseEntity.ok(projectService.updateProject(projectId, projectDto));
    }

    @DeleteMapping("/v1/{projectId}")
    public ResponseEntity<Void> delete(@PathVariable UUID projectId){
        projectService.deleteProject(projectId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/v1/{projectId}/department")
    public ResponseEntity<DepartmentDto> getDepartment(@PathVariable UUID projectId){
        return ResponseEntity.ok(projectService.getDepartment(projectId));
    }

    @GetMapping("/v1/{projectId}/teams")
    public ResponseEntity<List<TeamDto>> getTeams(@PathVariable UUID projectId){
        return ResponseEntity.ok(projectService.getTeams(projectId));
    }

    @GetMapping("/v1/{projectId}/tasks")
    public ResponseEntity<List<TaskDto>> getTasks(@PathVariable UUID projectId){
        return ResponseEntity.ok(projectService.getTasks(projectId));
    }

    @PostMapping("/v1/{projectId}/team/{teamId}/add")
    public ResponseEntity<TeamDto> addTeam(@PathVariable UUID projectId, @PathVariable UUID teamId){
        return ResponseEntity.ok(projectService.addTeam(projectId, teamId));
    }

    @PostMapping("/v1/{projectId}/team/{teamId}/discard")
    public ResponseEntity<TeamDto> discardTeam(@PathVariable UUID projectId, @PathVariable UUID teamId){
        return ResponseEntity.ok(projectService.discardTeam(projectId, teamId));
    }
}
