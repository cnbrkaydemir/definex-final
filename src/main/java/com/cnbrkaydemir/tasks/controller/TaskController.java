package com.cnbrkaydemir.tasks.controller;

import com.cnbrkaydemir.tasks.dto.*;
import com.cnbrkaydemir.tasks.model.TaskPriority;
import com.cnbrkaydemir.tasks.model.TaskProgress;
import com.cnbrkaydemir.tasks.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/v1")
    public ResponseEntity<List<TaskDto>> list(){
        return ResponseEntity.ok(taskService.getTasks());
    }

    @GetMapping("/v1/{taskId}")
    public ResponseEntity<TaskDto> get(@PathVariable UUID taskId){
        return ResponseEntity.ok(taskService.getTask(taskId));
    }

    @PostMapping("/v1")
    public ResponseEntity<TaskDto> create(@RequestBody CreateTaskDto taskDto){
        return ResponseEntity.created(URI.create("/api/task/v1/"+taskDto.getId()))
                .body(taskService.createTask(taskDto));
    }

    @PatchMapping("/v1/{taskId}")
    public ResponseEntity<TaskDto> update(@PathVariable UUID taskId, @RequestBody TaskDto taskdto){
        return ResponseEntity.ok(taskService.updateTask(taskId, taskdto));
    }

    @DeleteMapping("/v1/{taskId}")
    public ResponseEntity<Void> delete(@PathVariable UUID taskId){
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/v1/{taskId}/assignee")
    public ResponseEntity<UserDto> assignees(@PathVariable UUID taskId){
        return ResponseEntity.ok(taskService.getTaskAssignee(taskId));
    }

    @GetMapping("/v1/{taskId}/project")
    public ResponseEntity<ProjectDto> project(@PathVariable UUID taskId){
        return ResponseEntity.ok(taskService.getTaskProject(taskId));
    }

    @PatchMapping("/v1/{taskId}/progress")
    public ResponseEntity<TaskDto> progress(@PathVariable UUID taskId, @RequestBody TaskProgress taskProgress){
        return ResponseEntity.ok(taskService.updateTaskProgress(taskId, taskProgress));
    }

    @PatchMapping("/v1/{taskId}/priority")
    public ResponseEntity<TaskDto> priority(@PathVariable UUID taskId, @RequestBody TaskPriority taskPriority){
        return ResponseEntity.ok(taskService.updateTaskPriority(taskId, taskPriority));
    }

    @GetMapping("/v1/{taskId}/comments")
    public ResponseEntity<List<CommentDto>> comments(@PathVariable UUID taskId){
        return ResponseEntity.ok(taskService.getTaskComments(taskId));
    }

    @GetMapping("/v1/{taskId}/attachments")
    public ResponseEntity<List<AttachmentDto>> attachments(@PathVariable UUID taskId){
        return ResponseEntity.ok(taskService.getTaskAttachments(taskId));
    }
}
