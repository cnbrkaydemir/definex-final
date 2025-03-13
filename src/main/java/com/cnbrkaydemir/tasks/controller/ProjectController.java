package com.cnbrkaydemir.tasks.controller;

import com.cnbrkaydemir.tasks.dto.ProjectDto;
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
    public ResponseEntity<ProjectDto> create(@RequestBody ProjectDto projectDto){
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
}
