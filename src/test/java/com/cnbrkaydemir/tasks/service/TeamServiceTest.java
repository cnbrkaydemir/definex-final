package com.cnbrkaydemir.tasks.service;

import com.cnbrkaydemir.tasks.dto.*;
import com.cnbrkaydemir.tasks.exception.notfound.DepartmentNotFoundException;
import com.cnbrkaydemir.tasks.exception.notfound.TeamNotFoundException;
import com.cnbrkaydemir.tasks.exception.state.UserAlreadyInTeamException;
import com.cnbrkaydemir.tasks.exception.state.UserNotInTeamException;
import com.cnbrkaydemir.tasks.factory.DepartmentTestDataFactory;
import com.cnbrkaydemir.tasks.factory.ProjectTestDataFactory;
import com.cnbrkaydemir.tasks.factory.TeamTestDataFactory;
import com.cnbrkaydemir.tasks.factory.UsersTestDataFactory;
import com.cnbrkaydemir.tasks.model.Department;
import com.cnbrkaydemir.tasks.model.Project;
import com.cnbrkaydemir.tasks.model.Team;
import com.cnbrkaydemir.tasks.model.Users;
import com.cnbrkaydemir.tasks.repository.DepartmentRepository;
import com.cnbrkaydemir.tasks.repository.TeamRepository;
import com.cnbrkaydemir.tasks.repository.UsersRepository;
import com.cnbrkaydemir.tasks.service.impl.TeamServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TeamServiceTest {

    @InjectMocks
    private TeamServiceImpl teamService;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private ModelMapper modelMapper;

    @BeforeAll
    void setUp() {
        initMocks(this);
    }


    @Test
    void testGetTeams() {
        Team team = TeamTestDataFactory.createDefaultTeam();
        TeamDto teamDto = TeamTestDataFactory.createDtoFromTeam(team);
        List<Team> teamList = List.of(team);

        when(teamRepository.findAll()).thenReturn(teamList);
        when(modelMapper.map(team, TeamDto.class)).thenReturn(teamDto);

        List<TeamDto> retrievedTeams = teamService.getAllTeams();

        assertNotNull(retrievedTeams);
        assertFalse(retrievedTeams.isEmpty());
        verify(teamRepository).findAll();
    }

    @Test
    void testGetTeam() {
        Team team = TeamTestDataFactory.createDefaultTeam();
        TeamDto teamDto = TeamTestDataFactory.createDtoFromTeam(team);

        when(teamRepository.findById(team.getId())).thenReturn(Optional.of(team));
        when(modelMapper.map(team, TeamDto.class)).thenReturn(teamDto);

        TeamDto retrievedTeam = teamService.getTeamById(team.getId());

        assertNotNull(retrievedTeam);
        assertEquals(teamDto.getId(), retrievedTeam.getId());
        assertEquals(teamDto.getName(), retrievedTeam.getName());
        verify(teamRepository).findById(team.getId());
    }

    @Test
    void testGetTeamInvalidId() {
        UUID randomId = UUID.randomUUID();

        when(teamRepository.findById(randomId)).thenReturn(Optional.empty());
        assertThrows(TeamNotFoundException.class, () -> teamService.getTeamById(randomId));
    }

    @Test
    void testCreateTeam() {
        Team team = TeamTestDataFactory.createDefaultTeam();
        TeamDto teamDto = TeamTestDataFactory.createDtoFromTeam(team);
        Department department = DepartmentTestDataFactory.createDefaultDepartment();

        CreateTeamDto createTeamDto = new CreateTeamDto();
        createTeamDto.setId(team.getId());
        createTeamDto.setName(team.getName());
        createTeamDto.setDepartment(department.getId());

        when(departmentRepository.findById(department.getId())).thenReturn(Optional.of(department));
        when(teamRepository.save(team)).thenReturn(team);
        when(modelMapper.map(createTeamDto, Team.class)).thenReturn(team);
        when(modelMapper.map(team, TeamDto.class)).thenReturn(teamDto);

        TeamDto retrievedTeam = teamService.createTeam(createTeamDto);

        assertNotNull(retrievedTeam);
        assertEquals(teamDto.getId(), retrievedTeam.getId());
        assertEquals(teamDto.getName(), retrievedTeam.getName());
    }

    @Test
    void testCreateTeamInvalidDepartment() {
        UUID randomDepartmentId = UUID.randomUUID();

        when(departmentRepository.findById(randomDepartmentId)).thenReturn(Optional.empty());
        assertThrows(DepartmentNotFoundException.class, () -> teamService.createTeam(new CreateTeamDto()));
    }

    @Test
    void testUpdateTeam() {
        Team team = TeamTestDataFactory.createDefaultTeam();
        TeamDto teamDto = TeamTestDataFactory.createDtoFromTeam(team);
        teamDto.setGoal("Updated Goal");

        when(teamRepository.findById(team.getId())).thenReturn(Optional.of(team));
        when(modelMapper.map(team, TeamDto.class)).thenReturn(teamDto);
        when(teamRepository.save(team)).thenReturn(team);

        TeamDto retrievedTeam = teamService.updateTeam(team.getId(), teamDto);

        assertNotNull(retrievedTeam);
        assertEquals(teamDto.getId(), retrievedTeam.getId());
        assertEquals(teamDto.getName(), retrievedTeam.getName());
        assertEquals(teamDto.getGoal(), retrievedTeam.getGoal());
    }

    @Test
    void testUpdateTeamInvalidId() {
        Team team = TeamTestDataFactory.createDefaultTeam();
        TeamDto teamDto = TeamTestDataFactory.createDtoFromTeam(team);
        UUID randomId = UUID.randomUUID();

        when(teamRepository.findById(randomId)).thenReturn(Optional.empty());
        assertThrows(TeamNotFoundException.class, () -> teamService.updateTeam(randomId, teamDto));
    }

    @Test
    void testDeleteTeam() {
        Team team = TeamTestDataFactory.createDefaultTeam();
        TeamDto teamDto = TeamTestDataFactory.createDtoFromTeam(team);

        when(teamRepository.findById(team.getId())).thenReturn(Optional.of(team));
        when(modelMapper.map(team, TeamDto.class)).thenReturn(teamDto);
        when(teamRepository.save(team)).thenReturn(team);

        boolean isDeleted = teamService.deleteTeam(team.getId());

        assertTrue(isDeleted);
        assertTrue(team.isDeleted());
    }

    @Test
    void testDeleteTeamInvalidId() {
        UUID randomId = UUID.randomUUID();

        when(teamRepository.findById(randomId)).thenReturn(Optional.empty());
        assertThrows(TeamNotFoundException.class, () -> teamService.deleteTeam(randomId));
    }

    @Test
    void testGetUsers(){
        Team team = TeamTestDataFactory.createDefaultTeam();
        TeamDto teamDto = TeamTestDataFactory.createDtoFromTeam(team);
        Users user = UsersTestDataFactory.createDefaultUser();
        UserDto expectedUserDto = new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());
        List<Users> userList = List.of(user);

        when(teamRepository.findById(team.getId())).thenReturn(Optional.of(team));
        when(teamRepository.findActiveUsersByTeamId(team.getId())).thenReturn(userList);
        when(modelMapper.map(team, TeamDto.class)).thenReturn(teamDto);
        when(modelMapper.map(user, UserDto.class)).thenReturn(expectedUserDto);

        List<UserDto> retrievedUsers = teamService.getTeamUsers(team.getId());

        assertNotNull(retrievedUsers);
        assertFalse(retrievedUsers.isEmpty());
    }

    @Test
    void testGetUsersInvalidTeam(){
        UUID randomTeamId = UUID.randomUUID();

        when(teamRepository.findById(randomTeamId)).thenReturn(Optional.empty());
        assertThrows(TeamNotFoundException.class, () -> teamService.getTeamUsers(randomTeamId));
    }

    @Test
    void testGetDepartment(){
        Team team = TeamTestDataFactory.createDefaultTeam();
        TeamDto teamDto = TeamTestDataFactory.createDtoFromTeam(team);
        Department department = DepartmentTestDataFactory.createDefaultDepartment();
        DepartmentDto departmentDto = DepartmentTestDataFactory.dtoFromDepartment(department);

        when(teamRepository.findById(team.getId())).thenReturn(Optional.of(team));
        when(teamRepository.findActiveDepartmentByTeamId(team.getId())).thenReturn(department);
        when(modelMapper.map(department, DepartmentDto.class)).thenReturn(departmentDto);

        DepartmentDto retrievedDepartment = teamService.getTeamDepartment(team.getId());

        assertNotNull(retrievedDepartment);
        assertEquals(departmentDto.getId(), retrievedDepartment.getId());
        assertEquals(departmentDto.getName(), retrievedDepartment.getName());
    }

    @Test
    void testGetDepartmentInvalidId(){
        UUID randomId = UUID.randomUUID();

        when(teamRepository.findById(randomId)).thenReturn(Optional.empty());
        assertThrows(TeamNotFoundException.class, () -> teamService.getTeamDepartment(randomId));
    }

    @Test
    void testGetProject() {
        Team team = TeamTestDataFactory.createDefaultTeam();
        TeamDto teamDto = TeamTestDataFactory.createDtoFromTeam(team);
        Project project = ProjectTestDataFactory.createDefaultProject();
        ProjectDto projectDto = ProjectTestDataFactory.dtoFromProject(project);

        when(teamRepository.findById(team.getId())).thenReturn(Optional.of(team));
        when(teamRepository.findActiveProjectByTeamId(team.getId())).thenReturn(project);
        when(modelMapper.map(team, TeamDto.class)).thenReturn(teamDto);
        when(modelMapper.map(project, ProjectDto.class)).thenReturn(projectDto);

        ProjectDto retrievedProject = teamService.getTeamProject(team.getId());

        assertNotNull(retrievedProject);
        assertEquals(projectDto.getId(), retrievedProject.getId());
        assertEquals(projectDto.getName(), retrievedProject.getName());
    }

    @Test
    void testGetProjectInvalidId() {
        UUID randomId = UUID.randomUUID();
        when(teamRepository.findById(randomId)).thenReturn(Optional.empty());
        assertThrows(TeamNotFoundException.class, () -> teamService.getTeamProject(randomId));
    }

    @Test
    void testAddUserToTeam() throws Exception {
        Team team = TeamTestDataFactory.createDefaultTeam();
        Users user = UsersTestDataFactory.createDefaultUser();
        TeamDto teamDto = TeamTestDataFactory.createDtoFromTeam(team);

        when(teamRepository.findById(team.getId())).thenReturn(Optional.of(team));
        when(usersRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(teamRepository.save(team)).thenReturn(team);
        when(modelMapper.map(team, TeamDto.class)).thenReturn(teamDto);

        TeamDto result = teamService.addUserToTeam(team.getId(), user.getId());

        assertNotNull(result);
        assertTrue(team.getTeamMembers().contains(user));
    }

    @Test
    void testAddUserToTeamNotFound() {
        UUID teamId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        when(teamRepository.findById(teamId)).thenReturn(Optional.empty());

        assertThrows(TeamNotFoundException.class, () -> teamService.addUserToTeam(teamId, userId));
    }

    @Test
    void testAddUserAlreadyInTeam() {
        Team team = TeamTestDataFactory.createDefaultTeam();
        Users user = UsersTestDataFactory.createDefaultUser();
        team.getTeamMembers().add(user);

        when(teamRepository.findById(team.getId())).thenReturn(Optional.of(team));
        when(usersRepository.findById(user.getId())).thenReturn(Optional.of(user));

        assertThrows(UserAlreadyInTeamException.class, () -> teamService.addUserToTeam(team.getId(), user.getId()));
    }

    @Test
    void testDiscardUserFromTeam() throws Exception {
        Team team = TeamTestDataFactory.createDefaultTeam();
        Users user = UsersTestDataFactory.createDefaultUser();
        team.getTeamMembers().add(user);
        TeamDto teamDto = TeamTestDataFactory.createDtoFromTeam(team);

        when(teamRepository.findById(team.getId())).thenReturn(Optional.of(team));
        when(usersRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(teamRepository.save(team)).thenReturn(team);
        when(modelMapper.map(team, TeamDto.class)).thenReturn(teamDto);

        TeamDto result = teamService.discardUserFromTeam(team.getId(), user.getId());

        assertNotNull(result);
        assertFalse(team.getTeamMembers().contains(user));
    }

    @Test
    void testDiscardUserFromTeamNotFound() {
        UUID teamId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        when(teamRepository.findById(teamId)).thenReturn(Optional.empty());

        assertThrows(TeamNotFoundException.class, () -> teamService.discardUserFromTeam(teamId, userId));
    }

    @Test
    void testDiscardUserNotInTeam() {
        Team team = TeamTestDataFactory.createDefaultTeam();
        Users user = UsersTestDataFactory.createDefaultUser();

        when(teamRepository.findById(team.getId())).thenReturn(Optional.of(team));
        when(usersRepository.findById(user.getId())).thenReturn(Optional.of(user));

        assertThrows(UserNotInTeamException.class, () -> teamService.discardUserFromTeam(team.getId(), user.getId()));
    }
}

