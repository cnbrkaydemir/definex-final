package com.cnbrkaydemir.tasks.service;

import com.cnbrkaydemir.tasks.dto.*;
import com.cnbrkaydemir.tasks.exception.notfound.ProjectNotFoundException;
import com.cnbrkaydemir.tasks.exception.notfound.TaskNotFoundException;
import com.cnbrkaydemir.tasks.exception.notfound.UserNotFoundException;
import com.cnbrkaydemir.tasks.exception.state.TaskAlreadyAssignedException;
import com.cnbrkaydemir.tasks.factory.*;
import com.cnbrkaydemir.tasks.model.*;
import com.cnbrkaydemir.tasks.repository.ProjectRepository;
import com.cnbrkaydemir.tasks.repository.TaskRepository;
import com.cnbrkaydemir.tasks.repository.UsersRepository;
import com.cnbrkaydemir.tasks.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TaskServiceTest {

    @InjectMocks
    private TaskServiceImpl taskService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UsersRepository userRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private TaskProgressValidationService taskProgressValidationService;

    @Mock
    private ModelMapper modelMapper;

    @BeforeAll
    void setUp() {
        initMocks(this);
    }

    @Test
    void testGetAll() {
        Task task = TaskTestDataFactory.createDefaultTask();
        List<Task> tasks = List.of(task);
        TaskDto taskDto = TaskTestDataFactory.dtoFromTask(task);

        when(taskRepository.findAll()).thenReturn(tasks);
        when(modelMapper.map(task, TaskDto.class)).thenReturn(taskDto);

        List<TaskDto> allTasks = taskService.getTasks();

        assertNotNull(allTasks);
        assertEquals(allTasks.size(), tasks.size());
        assertEquals(allTasks.getFirst(), taskDto);
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void testGetTask() {
        Task task = TaskTestDataFactory.createDefaultTask();
        TaskDto taskDto = TaskTestDataFactory.dtoFromTask(task);

        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(modelMapper.map(task, TaskDto.class)).thenReturn(taskDto);

        TaskDto retrievedTask = taskService.getTask(task.getId());

        assertNotNull(retrievedTask);
        assertEquals(retrievedTask, taskDto);
        verify(taskRepository, times(1)).findById(task.getId());
    }

    @Test
    void testGetTaskInvalidId() {
        UUID randomId = UUID.randomUUID();
        when(taskRepository.findById(randomId)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.getTask(randomId));
    }

    @Test
    void testTaskCreation(){
        Task task = TaskTestDataFactory.createEmptyTask("Task 1", "Solve Defect 1",TaskProgress.CANCELLED, TaskPriority.LOW);
        TaskDto taskDto = TaskTestDataFactory.dtoFromTask(task);

        Users user = UsersTestDataFactory.createDefaultUserWithoutTask();
        Project project = ProjectTestDataFactory.createDefaultProject();

        CreateTaskDto createTaskDto = TaskTestDataFactory.createDefaultCreateTaskDto(user.getId(),project.getId(), taskDto);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));

        doNothing().when(taskProgressValidationService).validateReason(task.getProgress(), task.getReason());

        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(modelMapper.map(task, TaskDto.class)).thenReturn(taskDto);

        TaskDto newTask = taskService.createTask(createTaskDto);

        assertNotNull(newTask);
        assertEquals(newTask, taskDto);
    }


    @Test
    void testTaskCreationInvalidUserId(){
        Task task = TaskTestDataFactory.createEmptyTask("Task 1", "Solve Defect 1",TaskProgress.CANCELLED, TaskPriority.LOW);
        TaskDto taskDto = TaskTestDataFactory.dtoFromTask(task);

        UUID randomId = UUID.randomUUID();
        Project project = ProjectTestDataFactory.createDefaultProject();

        CreateTaskDto createTaskDto = TaskTestDataFactory.createDefaultCreateTaskDto(randomId,project.getId(), taskDto);

        when(userRepository.findById(randomId)).thenReturn(Optional.empty());
        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));

        assertThrows(UserNotFoundException.class, () -> taskService.createTask(createTaskDto));
    }

    @Test
    void testTaskCreationInvalidProjectId(){
        Task task = TaskTestDataFactory.createEmptyTask("Task 1", "Solve Defect 1",TaskProgress.CANCELLED, TaskPriority.LOW);
        TaskDto taskDto = TaskTestDataFactory.dtoFromTask(task);

        Users user = UsersTestDataFactory.createDefaultUserWithoutTask();
        UUID randomId = UUID.randomUUID();

        CreateTaskDto createTaskDto = TaskTestDataFactory.createDefaultCreateTaskDto(user.getId(),randomId, taskDto);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(projectRepository.findById(randomId)).thenReturn(Optional.empty());

        assertThrows(ProjectNotFoundException.class, () -> taskService.createTask(createTaskDto));
    }

    @Test
    void testDeleteTask() {
        Task task = TaskTestDataFactory.createDefaultTask();
        TaskDto taskDto = TaskTestDataFactory.dtoFromTask(task);

        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(modelMapper.map(task, TaskDto.class)).thenReturn(taskDto);

        boolean isDeleted = taskService.deleteTask(task.getId());

        assertTrue(isDeleted);
        assertTrue(task.isDeleted());
    }

    @Test
    void testDeleteTaskInvalidId() {
        UUID randomId = UUID.randomUUID();

        when(taskRepository.findById(randomId)).thenReturn(Optional.empty());
        assertThrows(TaskNotFoundException.class, () -> taskService.deleteTask(randomId));
    }

    @Test
    void testUpdateTask() {
        Task task = TaskTestDataFactory.createDefaultTask();
        TaskDto taskDto = TaskTestDataFactory.dtoFromTask(task);

        TaskDto updatedTaskDto = TaskTestDataFactory.dtoFromTask(task);
        updatedTaskDto.setProgress(TaskProgress.BLOCKED);
        updatedTaskDto.setReason("Blocked ");


        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(modelMapper.map(task, TaskDto.class)).thenReturn(taskDto);

        doNothing().when(taskProgressValidationService).validateReason(task.getProgress(), updatedTaskDto.getReason());
        when(taskProgressValidationService.validateTransition(task.getProgress(), updatedTaskDto.getProgress())).thenReturn(updatedTaskDto.getProgress());

        doAnswer(invocationOnMock -> {
            task.setProgress(updatedTaskDto.getProgress());
            task.setReason(updatedTaskDto.getReason());
            taskDto.setProgress(updatedTaskDto.getProgress());
            taskDto.setReason(updatedTaskDto.getReason());
            return null;
        }).
                when(modelMapper).map(updatedTaskDto, task);


        TaskDto updatedTask = taskService.updateTask(task.getId(), updatedTaskDto);

        assertNotNull(updatedTask);
        assertEquals(updatedTask, taskDto);
        assertEquals(updatedTask.getProgress(), updatedTaskDto.getProgress());
    }

    @Test
    void testUpdateTaskInvalidId() {
        Task task = TaskTestDataFactory.createDefaultTask();
        TaskDto taskDto = TaskTestDataFactory.dtoFromTask(task);
        UUID randomId = UUID.randomUUID();

        when(taskRepository.findById(randomId)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.updateTask(randomId, taskDto));
    }

    @Test
    void testGetTaskAssignee(){
        Task task = TaskTestDataFactory.createDefaultTask();
        Users user = UsersTestDataFactory.createDefaultUserWithoutTask();
        task.setAssignee(user);
        UserDto userDto = new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());

        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(taskRepository.findActiveUserByTaskId(task.getId())).thenReturn(user);
        when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);

        UserDto assignee = taskService.getTaskAssignee(task.getId());

        assertNotNull(assignee);
        assertEquals(assignee, userDto);
        assertEquals(assignee.getEmail(), userDto.getEmail());
        verify(taskRepository, times(1)).findActiveUserByTaskId(task.getId());
    }

    @Test
    void testGetTaskAssigneeInvalidId(){
        UUID randomId = UUID.randomUUID();

        when(taskRepository.findById(randomId)).thenReturn(Optional.empty());
        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskAssignee(randomId));
    }

    @Test
    void testGetTaskProject(){
        Task task = TaskTestDataFactory.createDefaultTask();
        Project project = ProjectTestDataFactory.createDefaultProject();
        task.setProject(project);
        ProjectDto projectDto = ProjectTestDataFactory.dtoFromProject(project);

        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(taskRepository.findActiveProjectByTaskId(task.getId())).thenReturn(project);
        when(modelMapper.map(project, ProjectDto.class)).thenReturn(projectDto);


        ProjectDto taskProject = taskService.getTaskProject(task.getId());

        assertNotNull(taskProject);
        assertEquals(taskProject,projectDto);
        verify(taskRepository, times(1)).findActiveProjectByTaskId(task.getId());
    }

    @Test
    void testGetTaskProjectInvalidId(){
        UUID randomId = UUID.randomUUID();

        when(taskRepository.findById(randomId)).thenReturn(Optional.empty());
        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskProject(randomId));
    }

    @Test
    void testGetTaskComments(){
        Task task = TaskTestDataFactory.createDefaultTask();
        Comment comment = CommentTestDataFactory.createComment();
        CommentDto commentDto = CommentTestDataFactory.dtoFromComment(comment);
        List<Comment> commentList = List.of(comment);

        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(taskRepository.findActiveCommentsByTaskId(task.getId())).thenReturn(commentList);
        when(modelMapper.map(comment, CommentDto.class)).thenReturn(commentDto);

        List<CommentDto> taskComments = taskService.getTaskComments(task.getId());

        assertNotNull(taskComments);
        assertEquals(taskComments.size(),commentList.size());
        assertEquals(taskComments.getFirst(),commentDto);
        verify(taskRepository, times(1)).findActiveCommentsByTaskId(task.getId());
    }

    @Test
    void testGetTaskCommentsInvalidId(){
        UUID randomId = UUID.randomUUID();

        when(taskRepository.findById(randomId)).thenReturn(Optional.empty());
        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskComments(randomId));
    }

    @Test
    void testGetTaskAttachments(){
        Task task = TaskTestDataFactory.createDefaultTask();
        Attachment attachment = AttachmentTestDataFactory.createAttachment();
        attachment.setTask(task);
        AttachmentDto attachmentDto = AttachmentTestDataFactory.dtoFromAttachment(attachment);
        List<Attachment> attachmentList = List.of(attachment);

        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(taskRepository.findActiveAttachmentsByTaskId(task.getId())).thenReturn(attachmentList);
        when(modelMapper.map(attachment, AttachmentDto.class)).thenReturn(attachmentDto);

        List<AttachmentDto> taskAttachments = taskService.getTaskAttachments(task.getId());

        assertNotNull(taskAttachments);
        assertEquals(taskAttachments.size(),attachmentList.size());
        assertEquals(taskAttachments.getFirst(),attachmentDto);
        verify(taskRepository, times(1)).findActiveAttachmentsByTaskId(task.getId());
    }

    @Test
    void testGetTaskAttachmentsInvalidId(){
        UUID randomId = UUID.randomUUID();

        when(taskRepository.findById(randomId)).thenReturn(Optional.empty());
        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskAttachments(randomId));
    }

    @Test
    void testUpdateTaskProgress(){
        Task task = TaskTestDataFactory.createDefaultTask();

        TaskProgress newProgress = TaskProgress.COMPLETED;
        String reason = "Completed";

        TaskDto updatedDto = TaskTestDataFactory.dtoFromTask(task);
        updatedDto.setProgress(newProgress);
        updatedDto.setReason(reason);

        UpdateTaskProgressDto updateTaskProgressDto = new UpdateTaskProgressDto();
        updateTaskProgressDto.setReason(reason);
        updateTaskProgressDto.setProgress(newProgress);
        task.setProgress(newProgress);

        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(modelMapper.map(task, TaskDto.class)).thenReturn(updatedDto);
        doNothing().when(taskProgressValidationService).validateReason(newProgress, reason);
        when(taskProgressValidationService.validateTransition(task.getProgress(), newProgress)).thenReturn(newProgress);
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));


        TaskDto updatedProgress = taskService.updateTaskProgress(task.getId(), updateTaskProgressDto);

        assertNotNull(updatedProgress);
        assertEquals(updatedProgress.getProgress(),TaskProgress.COMPLETED);
    }

    @Test
    void testUpdateTaskProgressInvalidId(){
        UUID randomId = UUID.randomUUID();
        TaskProgress newProgress = TaskProgress.COMPLETED;
        String reason = "Completed";

        UpdateTaskProgressDto updateTaskProgressDto = new UpdateTaskProgressDto();
        updateTaskProgressDto.setReason(reason);
        updateTaskProgressDto.setProgress(newProgress);

        when(taskRepository.findById(randomId)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.updateTaskProgress(randomId, updateTaskProgressDto));
    }

    @Test
    void testUpdateTaskPriority(){
        Task task = TaskTestDataFactory.createDefaultTask();

        TaskPriority newPriority = TaskPriority.CRITICAL;

        TaskDto updatedDto = TaskTestDataFactory.dtoFromTask(task);
        updatedDto.setPriority(newPriority);

        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(modelMapper.map(task, TaskDto.class)).thenReturn(updatedDto);
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TaskDto updatedPriority = taskService.updateTaskPriority(task.getId(), newPriority);

        assertNotNull(updatedPriority);
        assertEquals(updatedPriority.getPriority(),TaskPriority.CRITICAL);
    }

    @Test
    void testUpdateTaskPriorityInvalidId(){
        UUID randomId = UUID.randomUUID();
        TaskPriority newPriority = TaskPriority.CRITICAL;

        when(taskRepository.findById(randomId)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.updateTaskPriority(randomId, newPriority));
    }

    @Test
    void testAssignTaskToUser() {
        Task task = TaskTestDataFactory.createEmptyTask("Task x", "Solve Defect X", TaskProgress.CANCELLED, TaskPriority.LOW);
        Users user = UsersTestDataFactory.createDefaultUserWithoutTask();
        TaskDto taskDto = TaskTestDataFactory.dtoFromTask(task);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);
        when(modelMapper.map(task, TaskDto.class)).thenReturn(taskDto);

        TaskDto assignedTask = taskService.assignToUser(task.getId(), user.getId());

        assertNotNull(assignedTask);
        assertEquals(assignedTask.getId(),task.getId());
        assertEquals(assignedTask.getName(),task.getName());
    }

    @Test
    void testAssignTaskToUser_TaskAlreadyAssigned() {
        Task task = TaskTestDataFactory.createEmptyTask("Task x", "Solve Defect X", TaskProgress.CANCELLED, TaskPriority.LOW);
        Users user = UsersTestDataFactory.createDefaultUserWithoutTask();
        task.setAssignee(user);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        assertThrows(TaskAlreadyAssignedException.class, () -> taskService.assignToUser(task.getId(), user.getId()));
    }

    @Test
    void testAssignTaskToUser_TaskNotFound() {
        UUID randomId = UUID.randomUUID();
        Users user = UsersTestDataFactory.createDefaultUserWithoutTask();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(taskRepository.findById(randomId)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.assignToUser(randomId, user.getId()));
    }

    @Test
    void testAssignTaskToUser_UserNotFound() {
        Task task = TaskTestDataFactory.createEmptyTask("Task x", "Solve Defect X", TaskProgress.CANCELLED, TaskPriority.LOW);
        UUID randomId = UUID.randomUUID();

        when(userRepository.findById(randomId)).thenReturn(Optional.empty());
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        assertThrows(UserNotFoundException.class, () -> taskService.assignToUser(task.getId(), randomId));
    }

}
