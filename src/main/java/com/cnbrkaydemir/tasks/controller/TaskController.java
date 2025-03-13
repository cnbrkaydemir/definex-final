package com.cnbrkaydemir.tasks.controller;

import com.cnbrkaydemir.tasks.dto.TaskDto;
import com.cnbrkaydemir.tasks.model.Task;
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
    public ResponseEntity<TaskDto> create(@RequestBody TaskDto taskDto){
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

}
