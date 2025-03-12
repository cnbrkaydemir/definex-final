package com.cnbrkaydemir.tasks.service.impl;

import com.cnbrkaydemir.tasks.dto.TaskDto;
import com.cnbrkaydemir.tasks.exception.notfound.TaskNotFoundException;
import com.cnbrkaydemir.tasks.model.Task;
import com.cnbrkaydemir.tasks.repository.TaskRepository;
import com.cnbrkaydemir.tasks.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    private final ModelMapper modelMapper;

    @Override
    public TaskDto getTask(UUID id) throws TaskNotFoundException {
        Task targetTask = taskRepository.findById(id).orElseThrow(()-> new TaskNotFoundException(id));
        return modelMapper.map(targetTask, TaskDto.class);
    }

    @Override
    public List<TaskDto> getTasks() {
        return taskRepository.findAll()
                .stream()
                .map((task)->modelMapper.map(task, TaskDto.class))
                .toList();
    }

    @Override
    public TaskDto createTask(Task task) {
        Task newUser  = taskRepository.save(task);
        return modelMapper.map(newUser, TaskDto.class);
    }

    @Override
    public boolean deleteTask(UUID id) throws TaskNotFoundException {
        Task targetTask = taskRepository.findById(id).orElseThrow(()-> new TaskNotFoundException(id));
        taskRepository.delete(targetTask);
        return true;
    }

    @Override
    public TaskDto updateTask(UUID id, TaskDto task) throws TaskNotFoundException {
        Task oldTask = taskRepository.findById(id).orElseThrow(()-> new TaskNotFoundException(id));
        modelMapper.map(task, oldTask);
        return modelMapper.map(taskRepository.save(oldTask), TaskDto.class);
    }
}
