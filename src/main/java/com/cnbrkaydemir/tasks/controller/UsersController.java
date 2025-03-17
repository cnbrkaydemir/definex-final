package com.cnbrkaydemir.tasks.controller;

import com.cnbrkaydemir.tasks.dto.*;
import com.cnbrkaydemir.tasks.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;

    @GetMapping("/v1")
    public ResponseEntity<List<UserDto>> list(){
        return ResponseEntity.ok(usersService.getAll());
    }

    @GetMapping("/v1/{userId}")
    public ResponseEntity<UserDto> get(@PathVariable UUID userId){
        return ResponseEntity.ok(usersService.get(userId));
    }

    @PostMapping("/v1")
    public ResponseEntity<UserDto> create(@RequestBody UserDto userDto){
        return ResponseEntity.created(URI.create("/api/user/v1/"+userDto.getId()))
                .body(usersService.create(userDto));
    }

    @PatchMapping("/v1/{userId}")
    public ResponseEntity<UserDto> update(@PathVariable UUID userId, @RequestBody UserDto userDto){
        return ResponseEntity.ok(usersService.update(userId, userDto));
    }

    @DeleteMapping("/v1/{userId}")
    public ResponseEntity<Void> delete(@PathVariable UUID userId){
        usersService.delete(userId);
        return ResponseEntity.noContent()
                .build();
    }

    @GetMapping("/v1/{userId}/tasks")
    public ResponseEntity<List<TaskDto>> getTasks(@PathVariable UUID userId){
        return ResponseEntity.ok(usersService.getUserTasks(userId));
    }

    @GetMapping("/v1/{userId}/teams")
    public ResponseEntity<List<TeamDto>> getTeams(@PathVariable UUID userId){
        return ResponseEntity.ok(usersService.getUserTeams(userId));
    }

    @GetMapping("/v1/{userId}/comments")
    public ResponseEntity<List<CommentDto>> getComments(@PathVariable UUID userId){
        return ResponseEntity.ok(usersService.getUserComments(userId));
    }

    @GetMapping("/v1/{userId}/projects")
    public ResponseEntity<List<ProjectDto>> getProjects(@PathVariable UUID userId){
        return ResponseEntity.ok(usersService.getUserProjects(userId));
    }

}
