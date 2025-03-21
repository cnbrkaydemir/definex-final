package com.cnbrkaydemir.tasks.service;

import com.cnbrkaydemir.tasks.dto.*;
import com.cnbrkaydemir.tasks.exception.notfound.UserNotFoundException;
import com.cnbrkaydemir.tasks.exception.state.EmailAlreadyExistsException;
import com.cnbrkaydemir.tasks.factory.*;
import com.cnbrkaydemir.tasks.model.*;
import com.cnbrkaydemir.tasks.repository.UsersRepository;
import com.cnbrkaydemir.tasks.service.impl.UsersServiceImpl;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsersServiceTest {

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UsersServiceImpl usersService;

    @BeforeAll
    public void setUp() {
        initMocks(this);
    }



    @Test
    void testUserCreation() {
        Users user = UsersTestDataFactory.createCustomUser("Jane", "Doe", "j@gmail", "12345");
        UserDto expectedUserDto = new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());

        when(usersRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(usersRepository.save(any(Users.class))).thenReturn(user);
        when(modelMapper.map(user, UserDto.class)).thenReturn(expectedUserDto);

        UserDto savedUser = usersService.create(user);

        assertNotNull(savedUser);
        assertEquals(user.getFirstName(), savedUser.getFirstName());
        assertEquals(user.getLastName(), savedUser.getLastName());
        assertEquals(user.getEmail(), savedUser.getEmail());

        verify(passwordEncoder, times(1)).encode("12345");
        verify(modelMapper, times(1)).map(user, UserDto.class);
    }


    @Test
    void testUserCreationWithInvalidEmail(){
         Users duplicateUser = UsersTestDataFactory.createCustomUser("Jane", "Doe", "j@gmail", "password2");

        UserDto userDto = new UserDto(duplicateUser.getId(), duplicateUser.getFirstName(), duplicateUser.getLastName(), duplicateUser.getEmail());

        when(usersRepository.save(duplicateUser)).thenThrow(new EmailAlreadyExistsException(duplicateUser.getEmail()));
        when(modelMapper.map(duplicateUser, UserDto.class)).thenReturn(userDto);
        when(usersRepository.findByEmail(duplicateUser.getEmail())).thenReturn(Optional.of(duplicateUser));

        assertThrows(EmailAlreadyExistsException.class, () -> usersService.create(duplicateUser));
    }

    @Test
    void testGetAllUsers(){
        Users user = UsersTestDataFactory.createDefaultUser();
        UserDto expectedUserDto = new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());
        when(usersRepository.findAll()).thenReturn(List.of(user));
        when(modelMapper.map(user, UserDto.class)).thenReturn(expectedUserDto);

        List<UserDto> users = usersService.getAll();

        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals(user.getFirstName(), users.get(0).getFirstName());
        assertEquals(user.getLastName(), users.get(0).getLastName());
        assertEquals(user.getEmail(), users.get(0).getEmail());
        verify(usersRepository, times(1)).findAll();
    }

    @Test
    void testGetUser() {
        Users user = UsersTestDataFactory.createDefaultUser();
        UserDto expectedUserDto = new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());

        when(usersRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserDto.class)).thenReturn(expectedUserDto);

        UserDto retrievedUser = usersService.get(user.getId());

        assertNotNull(retrievedUser);
        assertEquals(user.getFirstName(), retrievedUser.getFirstName());

        verify(usersRepository, times(1)).findById(user.getId());
        verify(modelMapper, times(1)).map(user, UserDto.class);
    }

    @Test
    void testGetUserWithInvalidId(){
        UUID randomID = UUID.randomUUID();
        when(usersRepository.findById(randomID)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> usersService.get(randomID));
        verify(usersRepository, times(1)).findById(randomID);
    }

    @Test
    void testUpdateUser() {
        Users user = UsersTestDataFactory.createDefaultUserWithoutTask();
        UserDto updatedUserDto = new UserDto(user.getId(), user.getFirstName(), user.getLastName(), "new@email.com");
        Users updatedUser = UsersTestDataFactory.createDefaultUserWithoutTask();
        updatedUser.setId(user.getId());
        updatedUser.setEmail(updatedUserDto.getEmail());

        when(usersRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(usersRepository.save(any(Users.class))).thenReturn(updatedUser);
        when(modelMapper.map(user, UserDto.class)).thenReturn(updatedUserDto);
        when(modelMapper.map(updatedUser, UserDto.class)).thenReturn(updatedUserDto);


        doAnswer(invocationOnMock -> {
            user.setEmail(updatedUserDto.getEmail());
            return null;
        }).
        when(modelMapper).map(updatedUserDto, user);

        UserDto newUser = usersService.update(user.getId(), updatedUserDto);

        assertNotNull(newUser);
        assertEquals(user.getFirstName(), newUser.getFirstName());
        assertEquals(user.getLastName(), newUser.getLastName());
        assertEquals("new@email.com", newUser.getEmail());
        assertEquals(user.getId(), newUser.getId());
        verify(usersRepository, times(1)).findById(user.getId());

    }

    @Test
    void testUpdateUserInvalidId() {
        Users user = UsersTestDataFactory.createDefaultUserWithoutTask();
        UserDto updatedUserDto = new UserDto(user.getId(), user.getFirstName(), user.getLastName(), "new@email.com");
        UUID randomID = UUID.randomUUID();

        when(usersRepository.findById(randomID)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> usersService.update(randomID, updatedUserDto));
    }

    @Test
    void testDeleteUser() {
        Users user = UsersTestDataFactory.createDefaultUserWithoutTask();
        UserDto userDto = new UserDto(user.getId(), user.getFirstName(), user.getLastName(), "new@email.com");

        when(usersRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(usersRepository.save(any(Users.class))).thenReturn(user);

        boolean deleteResult = usersService.delete(user.getId());

        assertTrue(deleteResult);
        assertTrue(user.isDeleted());
    }

    @Test
    void testDeleteUserInvalidId() {
        UUID randomID = UUID.randomUUID();
        when(usersRepository.findById(randomID)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> usersService.delete(randomID));
    }

    @Test
    void testGetUserTasks() {
        Users user = UsersTestDataFactory.createDefaultUserWithoutTask();
        Task task = TaskTestDataFactory.createCustomTask("New Task", "Solve Defects", TaskProgress.CANCELLED, TaskPriority.LOW, user);
        TaskDto taskDto = TaskTestDataFactory.dtoFromTask(task);
        List<Task> tasks = List.of(task);
        user.setTasks(tasks);

        when(usersRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(usersRepository.findActiveTasksByUserId(user.getId())).thenReturn(tasks);
        when(modelMapper.map(task, TaskDto.class)).thenReturn(taskDto);

        List<TaskDto> userTasks = usersService.getUserTasks(user.getId());

        assertNotNull(userTasks);
        assertEquals(1, userTasks.size());
        assertEquals(taskDto.getId(), userTasks.getFirst().getId());
        verify(usersRepository, times(1)).findActiveTasksByUserId(user.getId());
    }

    @Test
    void testGetUserTasksInvalidId() {
        UUID randomID = UUID.randomUUID();

        when(usersRepository.findById(randomID)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> usersService.getUserTasks(randomID));
    }

    @Test
    void testGetUserTeams() {
        Users user = UsersTestDataFactory.createDefaultUserWithoutTask();
        Team team = TeamTestDataFactory.createCustomTeam("New Team", "Create Robust Systems");
        TeamDto teamDto = TeamTestDataFactory.createDtoFromTeam(team);
        List<Team> teams = List.of(team);
        user.setTeams(teams);

        when(usersRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(usersRepository.findActiveTeamsByUserId(user.getId())).thenReturn(teams);
        when(modelMapper.map(team, TeamDto.class)).thenReturn(teamDto);

        List<TeamDto> userTeams = usersService.getUserTeams(user.getId());

        assertNotNull(userTeams);
        assertEquals(1, userTeams.size());
        assertEquals(teamDto.getId(), userTeams.getFirst().getId());
        verify(usersRepository, times(1)).findActiveTeamsByUserId(user.getId());
    }

    @Test
    void testGetUserTeamsInvalidId() {
        UUID randomID = UUID.randomUUID();

        when(usersRepository.findById(randomID)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> usersService.getUserTeams(randomID));
    }

    @Test
    void testGetUserComments() {
        Users user = UsersTestDataFactory.createDefaultUserWithoutTask();
        Comment comment = CommentTestDataFactory.createComment();
        comment.setCommentedBy(user);
        CommentDto commentDto = CommentTestDataFactory.dtoFromComment(comment);
        List<Comment> comments = List.of(comment);
        user.setComments(comments);

        when(usersRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(usersRepository.findActiveCommentsByUserId(user.getId())).thenReturn(comments);
        when(modelMapper.map(comment, CommentDto.class)).thenReturn(commentDto);

        List<CommentDto> userComments = usersService.getUserComments(user.getId());

        assertNotNull(userComments);
        assertEquals(1, userComments.size());
        assertEquals(commentDto.getId(), userComments.getFirst().getId());
        verify(usersRepository, times(1)).findActiveCommentsByUserId(user.getId());
    }

    @Test
    void testGetUserCommentsInvalidId() {
        UUID randomID = UUID.randomUUID();

        when(usersRepository.findById(randomID)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> usersService.getUserComments(randomID));
    }

    @Test
    void testGetUserProjects() {
        Users user = UsersTestDataFactory.createDefaultUserWithoutTask();
        Project project = ProjectTestDataFactory.createCustomProject("New Project", "Renovation of Tasks");
        ProjectDto projectDto = ProjectTestDataFactory.dtoFromProject(project);
        List<Project> projects = List.of(project);

        when(usersRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(usersRepository.findActiveProjectsByUserId(user.getId())).thenReturn(projects);
        when(modelMapper.map(project, ProjectDto.class)).thenReturn(projectDto);

        List<ProjectDto> userProjects = usersService.getUserProjects(user.getId());

        assertNotNull(userProjects);
        assertEquals(1, userProjects.size());
        assertEquals(projectDto.getId(), userProjects.getFirst().getId());
        verify(usersRepository, times(1)).findActiveProjectsByUserId(user.getId());
    }

    @Test
    void testGetUserProjectsInvalidId() {
        UUID randomID = UUID.randomUUID();

        when(usersRepository.findById(randomID)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> usersService.getUserProjects(randomID));
    }



}
