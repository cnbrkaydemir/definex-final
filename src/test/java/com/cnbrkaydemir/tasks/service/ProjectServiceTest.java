package com.cnbrkaydemir.tasks.service;

import com.cnbrkaydemir.tasks.dto.*;
import com.cnbrkaydemir.tasks.exception.notfound.DepartmentNotFoundException;
import com.cnbrkaydemir.tasks.exception.notfound.ProjectNotFoundException;
import com.cnbrkaydemir.tasks.exception.notfound.TeamNotFoundException;
import com.cnbrkaydemir.tasks.exception.state.ProjectAlreadyContainsTeamException;
import com.cnbrkaydemir.tasks.exception.state.ProjectDoesNotIncludeTeamException;
import com.cnbrkaydemir.tasks.factory.DepartmentTestDataFactory;
import com.cnbrkaydemir.tasks.factory.ProjectTestDataFactory;
import com.cnbrkaydemir.tasks.factory.TaskTestDataFactory;
import com.cnbrkaydemir.tasks.factory.TeamTestDataFactory;
import com.cnbrkaydemir.tasks.model.Department;
import com.cnbrkaydemir.tasks.model.Project;
import com.cnbrkaydemir.tasks.model.Task;
import com.cnbrkaydemir.tasks.model.Team;
import com.cnbrkaydemir.tasks.repository.DepartmentRepository;
import com.cnbrkaydemir.tasks.repository.ProjectRepository;
import com.cnbrkaydemir.tasks.repository.TeamRepository;
import com.cnbrkaydemir.tasks.service.impl.ProjectServiceImpl;
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
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProjectServiceTest {

    @InjectMocks
    private ProjectServiceImpl projectService;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private ModelMapper modelMapper;

    @BeforeAll
    void setUp() {
        initMocks(this);
    }

    @Test
    void testGetAllProjects() {
        Project project = ProjectTestDataFactory.createDefaultProject();
        ProjectDto projectDto = ProjectTestDataFactory.dtoFromProject(project);
        List<Project> projectList = List.of(project);

        when(projectRepository.findAll()).thenReturn(projectList);
        when(modelMapper.map(project, ProjectDto.class)).thenReturn(projectDto);

        List<ProjectDto> retrievedProjects = projectService.getProjects();

        assertNotNull(retrievedProjects);
        assertEquals(projectDto, retrievedProjects.getFirst());
        assertFalse(retrievedProjects.isEmpty());
        verify(projectRepository, times(1)).findAll();
    }

    @Test
    void getProjectById() {
        Project project = ProjectTestDataFactory.createDefaultProject();
        ProjectDto projectDto = ProjectTestDataFactory.dtoFromProject(project);

        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));
        when(modelMapper.map(project, ProjectDto.class)).thenReturn(projectDto);

        ProjectDto retrievedProject = projectService.getProject(project.getId());

        assertNotNull(retrievedProject);
        assertEquals(projectDto, retrievedProject);
        verify(projectRepository, times(1)).findById(project.getId());
    }

    @Test
    void getProjectByInvalidId() {
        UUID randomId = UUID.randomUUID();

        when(projectRepository.findById(randomId)).thenReturn(Optional.empty());

        assertThrows(ProjectNotFoundException.class, () -> projectService.getProject(randomId));
    }

    @Test
    void testCreateProject() {
        Project project = ProjectTestDataFactory.createDefaultProject();
        ProjectDto projectDto = ProjectTestDataFactory.dtoFromProject(project);
        CreateProjectDto createProjectDto = ProjectTestDataFactory.createDefaultCreateProjectDto(projectDto);
        Department department = DepartmentTestDataFactory.createDefaultDepartment();
        createProjectDto.setDepartmentId(department.getId());

        when(departmentRepository.findById(department.getId())).thenReturn(Optional.of(department));
        when(projectRepository.save(project)).thenReturn(project);
        when(modelMapper.map(project, ProjectDto.class)).thenReturn(projectDto);
        when(modelMapper.map(createProjectDto, Project.class)).thenReturn(project);

        ProjectDto createdProject = projectService.createProject(createProjectDto);

        assertNotNull(createdProject);
        assertEquals(projectDto.getId(), createdProject.getId());
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    void testCreateProjectInvalidDepartment() {
        Project project = ProjectTestDataFactory.createDefaultProject();
        ProjectDto projectDto = ProjectTestDataFactory.dtoFromProject(project);
        CreateProjectDto createProjectDto = ProjectTestDataFactory.createDefaultCreateProjectDto(projectDto);
        UUID randomId = UUID.randomUUID();

        when(modelMapper.map(createProjectDto, Project.class)).thenReturn(project);
        when(departmentRepository.findById(randomId)).thenReturn(Optional.empty());

        assertThrows(DepartmentNotFoundException.class, () -> projectService.createProject(createProjectDto));
    }

    @Test
    void testDeleteProject() {
        Project project = ProjectTestDataFactory.createDefaultProject();
        ProjectDto projectDto = ProjectTestDataFactory.dtoFromProject(project);

        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));
        when(modelMapper.map(project, ProjectDto.class)).thenReturn(projectDto);

        boolean isDeleted = projectService.deleteProject(project.getId());

        assertTrue(isDeleted);
        assertTrue(project.isDeleted());
    }

    @Test
    void testDeleteProjectInvalidId() {
        UUID randomId = UUID.randomUUID();

        when(projectRepository.findById(randomId)).thenReturn(Optional.empty());
        assertThrows(ProjectNotFoundException.class, () -> projectService.deleteProject(randomId));
    }

    @Test
    void testUpdateProject() {
        Project project = ProjectTestDataFactory.createDefaultProject();
        ProjectDto projectDto = ProjectTestDataFactory.dtoFromProject(project);
        ProjectDto updatedProjectDto = ProjectTestDataFactory.dtoFromProject(project);
        updatedProjectDto.setDescription("Updated Description");

        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));
        when(projectRepository.save(any(Project.class))).thenReturn(project);
        when(modelMapper.map(project, ProjectDto.class)).thenReturn(projectDto);
        doAnswer(invocationOnMock -> {
            project.setDescription(updatedProjectDto.getDescription());
            projectDto.setDescription(updatedProjectDto.getDescription());
            return null;
        }).
                when(modelMapper).map(updatedProjectDto, project);

        ProjectDto updatedProject = projectService.updateProject(project.getId(), updatedProjectDto);

        assertNotNull(updatedProject);
        assertEquals(updatedProjectDto.getDescription(), updatedProject.getDescription());
        assertEquals(updatedProjectDto.getId(), updatedProject.getId());
    }

    @Test
    void testUpdateProjectInvalidId() {
        UUID randomId = UUID.randomUUID();
        Project project = ProjectTestDataFactory.createDefaultProject();
        ProjectDto projectDto = ProjectTestDataFactory.dtoFromProject(project);

        when(projectRepository.findById(randomId)).thenReturn(Optional.empty());
        assertThrows(ProjectNotFoundException.class, () -> projectService.updateProject(randomId, projectDto));
    }

    @Test
    void testGetDepartment() {
        Project project = ProjectTestDataFactory.createDefaultProject();
        Department department = DepartmentTestDataFactory.createDefaultDepartment();
        DepartmentDto departmentDto = DepartmentTestDataFactory.dtoFromDepartment(department);

        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));
        when(projectRepository.findActiveDepartmentByProjectId(project.getId())).thenReturn(department);
        when(modelMapper.map(department, DepartmentDto.class)).thenReturn(departmentDto);

        DepartmentDto retrievedDepartment = projectService.getDepartment(project.getId());

        assertNotNull(retrievedDepartment);
        assertEquals(departmentDto, retrievedDepartment);
    }
    @Test
    void testGetDepartmentInvalidId() {
        UUID randomId = UUID.randomUUID();
        Department department = DepartmentTestDataFactory.createDefaultDepartment();
        DepartmentDto departmentDto = DepartmentTestDataFactory.dtoFromDepartment(department);

        when(projectRepository.findById(randomId)).thenReturn(Optional.empty());
        when(modelMapper.map(department, DepartmentDto.class)).thenReturn(departmentDto);

        assertThrows(ProjectNotFoundException.class,() -> projectService.getDepartment(randomId));
    }

    @Test
    void testGetTeams() {
        Project project = ProjectTestDataFactory.createDefaultProject();
        ProjectDto projectDto = ProjectTestDataFactory.dtoFromProject(project);
        Team team = TeamTestDataFactory.createDefaultTeam();
        TeamDto teamDto = TeamTestDataFactory.createDtoFromTeam(team);
        List<Team> teamList = List.of(team);

        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));
        when(projectRepository.findActiveTeamsByProjectId(project.getId())).thenReturn(teamList);
        when(modelMapper.map(project, ProjectDto.class)).thenReturn(projectDto);
        when(modelMapper.map(team, TeamDto.class)).thenReturn(teamDto);

        List<TeamDto> teamDtoList = projectService.getTeams(project.getId());

        assertNotNull(teamDtoList);
        assertEquals(teamDtoList.size(), teamList.size());
        assertEquals(teamDtoList.getFirst(), teamDto);
    }

    @Test
    void testGetTeamsInvalidId() {
        UUID randomId = UUID.randomUUID();

        when(projectRepository.findById(randomId)).thenReturn(Optional.empty());
        assertThrows(ProjectNotFoundException.class,() -> projectService.getTeams(randomId));
    }


    @Test
    void testGetTasks() {
        Project project = ProjectTestDataFactory.createDefaultProject();
        ProjectDto projectDto = ProjectTestDataFactory.dtoFromProject(project);
        Task task = TaskTestDataFactory.createDefaultTask();
        TaskDto taskDto = TaskTestDataFactory.dtoFromTask(task);
        List<Task> taskList = List.of(task);

        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));
        when(projectRepository.findActiveTasksByProjectId(project.getId())).thenReturn(taskList);
        when(modelMapper.map(task, TaskDto.class)).thenReturn(taskDto);

        List<TaskDto> taskDtoList = projectService.getTasks(project.getId());

        assertNotNull(taskDtoList);
        assertEquals(taskDtoList.size(), taskList.size());
        assertEquals(taskDtoList.getFirst(), taskDto);
    }

    @Test
    void testGetTasksInvalidId() {
        UUID randomId = UUID.randomUUID();

        when(projectRepository.findById(randomId)).thenReturn(Optional.empty());
        assertThrows(ProjectNotFoundException.class,() -> projectService.getTasks(randomId));
    }

    @Test
    void testAddTeam() {
        Project project = ProjectTestDataFactory.createDefaultProject();
        ProjectDto projectDto = ProjectTestDataFactory.dtoFromProject(project);
        Team team = TeamTestDataFactory.createDefaultTeam();
        TeamDto teamDto = TeamTestDataFactory.createDtoFromTeam(team);

        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));
        when(teamRepository.findById(team.getId())).thenReturn(Optional.of(team));
        when(modelMapper.map(team, TeamDto.class)).thenReturn(teamDto);

        TeamDto addedTeam = projectService.addTeam(project.getId(), team.getId());

        assertNotNull(addedTeam);
        assertEquals(teamDto, addedTeam);
        assertEquals(teamDto.getId(), addedTeam.getId());
        assertTrue(project.getTeams().contains(team));
    }

    @Test
    void testAddTeamAlreadyExists() {
        Project project = ProjectTestDataFactory.createDefaultProject();
        ProjectDto projectDto = ProjectTestDataFactory.dtoFromProject(project);
        Team team = TeamTestDataFactory.createDefaultTeam();
        TeamDto teamDto = TeamTestDataFactory.createDtoFromTeam(team);
        project.getTeams().add(team);

        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));
        when(teamRepository.findById(team.getId())).thenReturn(Optional.of(team));
        when(modelMapper.map(team, TeamDto.class)).thenReturn(teamDto);

        assertThrows(ProjectAlreadyContainsTeamException.class,() -> projectService.addTeam(project.getId(), team.getId()));
    }

    @Test
    void testAddTeamInvalidProject() {
        UUID randomId = UUID.randomUUID();
        Team team = TeamTestDataFactory.createDefaultTeam();
        TeamDto teamDto = TeamTestDataFactory.createDtoFromTeam(team);

        when(projectRepository.findById(randomId)).thenReturn(Optional.empty());
        when(teamRepository.findById(team.getId())).thenReturn(Optional.of(team));
        when(modelMapper.map(team, TeamDto.class)).thenReturn(teamDto);

        assertThrows(ProjectNotFoundException.class,() -> projectService.addTeam(randomId, team.getId()));
    }

    @Test
    void testAddTeamInvalidTeam() {
        Project project = ProjectTestDataFactory.createDefaultProject();
        UUID randomId = UUID.randomUUID();

        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));
        when(teamRepository.findById(randomId)).thenReturn(Optional.empty());
        assertThrows(TeamNotFoundException.class,() -> projectService.addTeam(project.getId(), randomId));
    }

    @Test
    void testDiscardTeam() {
        Project project = ProjectTestDataFactory.createDefaultProject();
        ProjectDto projectDto = ProjectTestDataFactory.dtoFromProject(project);
        Team team = TeamTestDataFactory.createDefaultTeam();
        TeamDto teamDto = TeamTestDataFactory.createDtoFromTeam(team);
        project.getTeams().add(team);

        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));
        when(teamRepository.findById(team.getId())).thenReturn(Optional.of(team));
        when(modelMapper.map(team, TeamDto.class)).thenReturn(teamDto);

        TeamDto addedTeam = projectService.discardTeam(project.getId(), team.getId());

        assertNotNull(addedTeam);
        assertEquals(teamDto, addedTeam);
        assertFalse(project.getTeams().contains(team));
    }

    @Test
    void testAddTeamDoesNotExists() {
        Project project = ProjectTestDataFactory.createDefaultProject();
        ProjectDto projectDto = ProjectTestDataFactory.dtoFromProject(project);
        Team team = TeamTestDataFactory.createDefaultTeam();
        TeamDto teamDto = TeamTestDataFactory.createDtoFromTeam(team);

        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));
        when(teamRepository.findById(team.getId())).thenReturn(Optional.of(team));
        when(modelMapper.map(team, TeamDto.class)).thenReturn(teamDto);

        assertThrows(ProjectDoesNotIncludeTeamException.class,() -> projectService.discardTeam(project.getId(), team.getId()));
    }

    @Test
    void testDiscardTeamInvalidProject() {
        UUID randomId = UUID.randomUUID();
        Team team = TeamTestDataFactory.createDefaultTeam();
        TeamDto teamDto = TeamTestDataFactory.createDtoFromTeam(team);

        when(projectRepository.findById(randomId)).thenReturn(Optional.empty());
        when(teamRepository.findById(team.getId())).thenReturn(Optional.of(team));
        when(modelMapper.map(team, TeamDto.class)).thenReturn(teamDto);

        assertThrows(ProjectNotFoundException.class,() -> projectService.discardTeam(randomId, team.getId()));
    }

    @Test
    void testDiscardTeamInvalidTeam() {
        Project project = ProjectTestDataFactory.createDefaultProject();
        UUID randomId = UUID.randomUUID();

        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));
        when(teamRepository.findById(randomId)).thenReturn(Optional.empty());
        assertThrows(TeamNotFoundException.class,() -> projectService.discardTeam(project.getId(), randomId));
    }
}
