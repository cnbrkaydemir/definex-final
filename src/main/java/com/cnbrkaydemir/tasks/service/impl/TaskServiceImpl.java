package com.cnbrkaydemir.tasks.service.impl;

import com.cnbrkaydemir.tasks.dto.*;
import com.cnbrkaydemir.tasks.exception.notfound.ProjectNotFoundException;
import com.cnbrkaydemir.tasks.exception.notfound.TaskNotFoundException;
import com.cnbrkaydemir.tasks.exception.notfound.TeamNotFoundException;
import com.cnbrkaydemir.tasks.exception.notfound.UserNotFoundException;
import com.cnbrkaydemir.tasks.exception.state.TaskAlreadyAssignedException;
import com.cnbrkaydemir.tasks.model.*;
import com.cnbrkaydemir.tasks.repository.ProjectRepository;
import com.cnbrkaydemir.tasks.repository.TaskRepository;
import com.cnbrkaydemir.tasks.repository.UsersRepository;
import com.cnbrkaydemir.tasks.service.TaskProgressValidationService;
import com.cnbrkaydemir.tasks.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    private final UsersRepository userRepository;

    private final ProjectRepository projectRepository;

    private final TaskProgressValidationService taskProgressValidationService;

    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public TaskDto getTask(UUID id) throws TaskNotFoundException {
        Task targetTask = taskRepository.findById(id).orElseThrow(()-> new TaskNotFoundException(id));
        return modelMapper.map(targetTask, TaskDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskDto> getTasks() {
        return taskRepository.findAll()
                .stream()
                .map((task)->modelMapper.map(task, TaskDto.class))
                .toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TaskDto createTask(CreateTaskDto taskDto) throws UserNotFoundException, TeamNotFoundException {
        Users user = userRepository.findById(taskDto.getUserId()).orElseThrow(()-> new UserNotFoundException(taskDto.getUserId()));
        Project project = projectRepository.findById(taskDto.getProjectId()).orElseThrow(()-> new ProjectNotFoundException(taskDto.getProjectId()));

        taskProgressValidationService.validateReason(taskDto.getProgress(), taskDto.getReason());

        Task task = CreateTaskDto.convertToTask(taskDto);
        task.setProject(project);
        task.setAssignee(user);
        task.setDeleted(false);
        return modelMapper.map(taskRepository.save(task), TaskDto.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteTask(UUID id) throws TaskNotFoundException {
        Task targetTask = taskRepository.findById(id).orElseThrow(()-> new TaskNotFoundException(id));
        targetTask.setDeleted(true);
        taskRepository.save(targetTask);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TaskDto updateTask(UUID id, TaskDto task) throws TaskNotFoundException {
        Task oldTask = taskRepository.findById(id).orElseThrow(()-> new TaskNotFoundException(id));

        taskProgressValidationService.validateTransition(oldTask.getProgress(), task.getProgress());
        taskProgressValidationService.validateReason(task.getProgress(), task.getReason());

        modelMapper.map(task, oldTask);
        return modelMapper.map(taskRepository.save(oldTask), TaskDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getTaskAssignee(UUID id) throws TaskNotFoundException {
        Task task = taskRepository.findById(id).orElseThrow(()-> new TaskNotFoundException(id));
        Users targetUser = taskRepository.findActiveUserByTaskId(task.getId());
        return modelMapper.map(targetUser, UserDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectDto getTaskProject(UUID id) throws TaskNotFoundException {
        Task task = taskRepository.findById(id).orElseThrow(()-> new TaskNotFoundException(id));
        Project project = taskRepository.findActiveProjectByTaskId(task.getId());
        return modelMapper.map(project, ProjectDto.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TaskDto updateTaskPriority(UUID id, TaskPriority priority) throws TaskNotFoundException {
        Task task = taskRepository.findById(id).orElseThrow(()-> new TaskNotFoundException(id));
        task.setPriority(priority);
        return modelMapper.map(taskRepository.save(task), TaskDto.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TaskDto updateTaskProgress(UUID id, UpdateTaskProgressDto progressDto) throws TaskNotFoundException {
        Task task = taskRepository.findById(id).orElseThrow(()-> new TaskNotFoundException(id));
        taskProgressValidationService.validateTransition(task.getProgress(), progressDto.getProgress());
        taskProgressValidationService.validateReason(progressDto.getProgress(), progressDto.getReason());
        task.setProgress(progressDto.getProgress());
        return modelMapper.map(taskRepository.save(task), TaskDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> getTaskComments(UUID id) throws TaskNotFoundException {
        Task task = taskRepository.findById(id).orElseThrow(()-> new TaskNotFoundException(id));
        return taskRepository.findActiveCommentsByTaskId(task.getId())
                .stream()
                .map(comment -> modelMapper.map(comment, CommentDto.class))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttachmentDto> getTaskAttachments(UUID id) throws TaskNotFoundException {
        Task task = taskRepository.findById(id).orElseThrow(()-> new TaskNotFoundException(id));
        return taskRepository.findActiveAttachmentsByTaskId(task.getId())
                .stream()
                .map(attachment -> modelMapper.map(attachment, AttachmentDto.class))
                .toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TaskDto assignToUser(UUID taskId, UUID userId) throws TaskNotFoundException, TaskAlreadyAssignedException {
        Task task = taskRepository.findById(taskId).orElseThrow(()-> new TaskNotFoundException(taskId));
        Users user = userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException(userId));

        if (task.getAssignee() == null){
            task.setAssignee(user);
            return modelMapper.map(taskRepository.save(task), TaskDto.class);
        }
        throw  new TaskAlreadyAssignedException(task.getName());
    }
}
