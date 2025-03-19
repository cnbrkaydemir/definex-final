package com.cnbrkaydemir.tasks.controller;

import com.cnbrkaydemir.tasks.dto.DepartmentDto;
import com.cnbrkaydemir.tasks.dto.ProjectDto;
import com.cnbrkaydemir.tasks.dto.TeamDto;
import com.cnbrkaydemir.tasks.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/department")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @GetMapping("/v1")
    public ResponseEntity<List<DepartmentDto>> list(){
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }

    @GetMapping("/v1/{departmentId}")
    public ResponseEntity<DepartmentDto> get(@PathVariable UUID departmentId){
        return ResponseEntity.ok(departmentService.getDepartmentById(departmentId));
    }

    @PostMapping("/v1")
    public ResponseEntity<DepartmentDto> create(@RequestBody DepartmentDto departmentDto){
        return ResponseEntity.created(URI.create("/api/department/" + departmentDto.getId()))
                .body(departmentService.createDepartment(departmentDto));
    }

    @PatchMapping("/v1/{departmentId}")
    public ResponseEntity<DepartmentDto> update(@PathVariable UUID departmentId, @RequestBody DepartmentDto departmentDto){
        return ResponseEntity.ok(departmentService.updateDepartment(departmentId, departmentDto));
    }

    @DeleteMapping("/v1/{departmentId}")
    public ResponseEntity<Void> delete(@PathVariable UUID departmentId){
        departmentService.deleteDepartment(departmentId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/v1/{departmentId}/teams")
    public ResponseEntity<List<TeamDto>> getTeams(@PathVariable UUID departmentId){
        return ResponseEntity.ok(departmentService.getTeamsByDepartment(departmentId));
    }

    @GetMapping("/v1/{departmentId}/projects")
    public ResponseEntity<List<ProjectDto>> getProjects(@PathVariable UUID departmentId){
        return ResponseEntity.ok(departmentService.getProjectsByDepartment(departmentId));
    }

    @PostMapping("/v1/{departmentId}/team/{teamId}/add")
    public ResponseEntity<TeamDto> addTeam(@PathVariable UUID departmentId, @PathVariable UUID teamId){
        return ResponseEntity.ok(departmentService.addTeam(departmentId, teamId));
    }

    @PostMapping("/v1/{departmentId}/team/{teamId}/discard")
    public ResponseEntity<TeamDto> discardTeam(@PathVariable UUID departmentId, @PathVariable UUID teamId){
        return ResponseEntity.ok(departmentService.discardTeam(departmentId, teamId));
    }

    @PostMapping("/v1/{departmentId}/project/{projectId}/add")
    public ResponseEntity<ProjectDto> addProject(@PathVariable UUID departmentId, @PathVariable UUID projectId){
        return ResponseEntity.ok(departmentService.addProject(departmentId, projectId));
    }

    @PostMapping("/v1/{departmentId}/project/{projectId}/discard")
    public ResponseEntity<ProjectDto> discardProject(@PathVariable UUID departmentId, @PathVariable UUID projectId){
        return ResponseEntity.ok(departmentService.discardProject(departmentId, projectId));
    }
}
