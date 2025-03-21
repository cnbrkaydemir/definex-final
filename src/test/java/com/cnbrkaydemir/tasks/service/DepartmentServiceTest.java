package com.cnbrkaydemir.tasks.service;

import com.cnbrkaydemir.tasks.dto.DepartmentDto;
import com.cnbrkaydemir.tasks.dto.ProjectDto;
import com.cnbrkaydemir.tasks.dto.TeamDto;
import com.cnbrkaydemir.tasks.exception.notfound.DepartmentNotFoundException;
import com.cnbrkaydemir.tasks.exception.notfound.ProjectNotFoundException;
import com.cnbrkaydemir.tasks.exception.notfound.TeamNotFoundException;
import com.cnbrkaydemir.tasks.exception.state.DepartmentAlreadyContainsProjectException;
import com.cnbrkaydemir.tasks.exception.state.DepartmentAlreadyContainsTeamException;
import com.cnbrkaydemir.tasks.exception.state.DepartmentDoesNotContainProjectException;
import com.cnbrkaydemir.tasks.exception.state.DepartmentDoesNotContainTeamException;
import com.cnbrkaydemir.tasks.factory.DepartmentTestDataFactory;
import com.cnbrkaydemir.tasks.factory.ProjectTestDataFactory;
import com.cnbrkaydemir.tasks.factory.TeamTestDataFactory;
import com.cnbrkaydemir.tasks.model.Department;
import com.cnbrkaydemir.tasks.model.Project;
import com.cnbrkaydemir.tasks.model.Team;
import com.cnbrkaydemir.tasks.repository.DepartmentRepository;
import com.cnbrkaydemir.tasks.repository.ProjectRepository;
import com.cnbrkaydemir.tasks.repository.TeamRepository;
import com.cnbrkaydemir.tasks.service.impl.DepartmentServiceImpl;
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
public class DepartmentServiceTest {

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private ModelMapper modelMapper;

    @BeforeAll
    void setUp(){
        initMocks(this);
    }

    @Test
    void testGetDepartments(){
        Department department = DepartmentTestDataFactory.createDefaultDepartment();
        DepartmentDto departmentDto = DepartmentTestDataFactory.dtoFromDepartment(department);
        List<Department> departments = List.of(department);

        when(departmentRepository.findAll()).thenReturn(departments);
        when(modelMapper.map(department, DepartmentDto.class)).thenReturn(departmentDto);

        List<DepartmentDto> allDepartments = departmentService.getAllDepartments();

        assertNotNull(allDepartments);
        assertFalse(allDepartments.isEmpty());
        assertEquals(departmentDto, allDepartments.getFirst());
        verify(departmentRepository, times(1)).findAll();
    }

    @Test
    void testGetDepartmentById(){
        Department department = DepartmentTestDataFactory.createDefaultDepartment();
        DepartmentDto departmentDto = DepartmentTestDataFactory.dtoFromDepartment(department);

        when(departmentRepository.findById(department.getId())).thenReturn(Optional.of(department));
        when(modelMapper.map(department, DepartmentDto.class)).thenReturn(departmentDto);

        DepartmentDto departmentDtoById = departmentService.getDepartmentById(department.getId());

        assertNotNull(departmentDtoById);
        assertEquals(departmentDto, departmentDtoById);
        verify(departmentRepository, times(1)).findById(department.getId());
    }

    @Test
    void testGetDepartmentByInvalidId(){
        UUID randomId = UUID.randomUUID();

        when(departmentRepository.findById(randomId)).thenReturn(Optional.empty());
        assertThrows(DepartmentNotFoundException.class, () -> departmentService.getDepartmentById(randomId));
    }

    @Test
    void testDepartmentCreation(){
        Department department = DepartmentTestDataFactory.createDefaultDepartment();
        DepartmentDto departmentDto = DepartmentTestDataFactory.dtoFromDepartment(department);


        when(departmentRepository.save(department)).thenReturn(department);
        when(modelMapper.map(departmentDto, Department.class)).thenReturn(department);
        when(modelMapper.map(department, DepartmentDto.class)).thenReturn(departmentDto);

        DepartmentDto departmentDtoById = departmentService.createDepartment(departmentDto);

        assertNotNull(departmentDtoById);
        assertEquals(departmentDto, departmentDtoById);
        verify(departmentRepository, times(1)).save(department);
    }

    @Test
    void testUpdateDepartment(){
        Department department = DepartmentTestDataFactory.createDefaultDepartment();
        DepartmentDto departmentDto = DepartmentTestDataFactory.dtoFromDepartment(department);

        when(departmentRepository.findById(department.getId())).thenReturn(Optional.of(department));
        when(departmentRepository.save(department)).thenReturn(department);
        when(modelMapper.map(department, DepartmentDto.class)).thenReturn(departmentDto);

        DepartmentDto updatedDepartmentDto = departmentService.updateDepartment(department.getId(), departmentDto);

        assertNotNull(updatedDepartmentDto);
        assertEquals(departmentDto, updatedDepartmentDto);
        verify(departmentRepository).save(department);
    }

    @Test
    void testUpdateDepartmentInvalidId(){
        UUID randomId = UUID.randomUUID();
        Department department = DepartmentTestDataFactory.createDefaultDepartment();
        DepartmentDto departmentDto = DepartmentTestDataFactory.dtoFromDepartment(department);

        when(departmentRepository.findById(randomId)).thenReturn(Optional.empty());

        assertThrows(DepartmentNotFoundException.class, ()-> departmentService.updateDepartment(randomId, departmentDto));
    }

    @Test
    void testDeleteDepartment(){
        Department department = DepartmentTestDataFactory.createDefaultDepartment();
        DepartmentDto departmentDto = DepartmentTestDataFactory.dtoFromDepartment(department);

        when(departmentRepository.findById(department.getId())).thenReturn(Optional.of(department));

        boolean isDeleted = departmentService.deleteDepartment(department.getId());

        assertTrue(isDeleted);
        assertTrue(department.isDeleted());
    }

    @Test
    void testDeleteDepartmentInvalidId(){
        UUID randomId = UUID.randomUUID();

        when(departmentRepository.findById(randomId)).thenReturn(Optional.empty());

        assertThrows(DepartmentNotFoundException.class, ()-> departmentService.deleteDepartment(randomId));
    }

    @Test
    void testGetTeamsFromDepartment(){
        Department department = DepartmentTestDataFactory.createDefaultDepartment();
        DepartmentDto departmentDto = DepartmentTestDataFactory.dtoFromDepartment(department);
        Team team = TeamTestDataFactory.createDefaultTeam();
        TeamDto teamDto = TeamTestDataFactory.createDtoFromTeam(team);
        List<Team> teamList = List.of(team);

        when(departmentRepository.findById(department.getId())).thenReturn(Optional.of(department));
        when(departmentRepository.findActiveTeamsByDepartmentId(department.getId())).thenReturn(teamList);
        when(modelMapper.map(team, TeamDto.class)).thenReturn(teamDto);

        List<TeamDto> departmentTeams = departmentService.getTeamsByDepartment(department.getId());

        assertNotNull(departmentTeams);
        assertFalse(departmentTeams.isEmpty());
        assertEquals(teamDto, departmentTeams.getFirst());
        verify(departmentRepository, times(1)).findActiveTeamsByDepartmentId(department.getId());
    }

    @Test
    void testGetTeamsFromDepartmentInvalidId(){
        UUID randomId = UUID.randomUUID();

        when(departmentRepository.findById(randomId)).thenReturn(Optional.empty());
        assertThrows(DepartmentNotFoundException.class, () -> departmentService.getTeamsByDepartment(randomId));
    }

    @Test
    void testGetProjectsFromDepartment(){
        Department department = DepartmentTestDataFactory.createDefaultDepartment();
        DepartmentDto departmentDto = DepartmentTestDataFactory.dtoFromDepartment(department);

        Project project = ProjectTestDataFactory.createDefaultProject();
        List<Project> projectList = List.of(project);
        ProjectDto projectDto = ProjectTestDataFactory.dtoFromProject(project);

        when(departmentRepository.findById(department.getId())).thenReturn(Optional.of(department));
        when(departmentRepository.findActiveProjectsByDepartmentId(department.getId())).thenReturn(projectList);
        when(modelMapper.map(project, ProjectDto.class)).thenReturn(projectDto);

        List<ProjectDto> departmentProjects = departmentService.getProjectsByDepartment(department.getId());

        assertNotNull(departmentProjects);
        assertFalse(departmentProjects.isEmpty());
        assertEquals(projectDto, departmentProjects.getFirst());
        verify(departmentRepository, times(1)).findActiveProjectsByDepartmentId(department.getId());
    }

    @Test
    void testGetProjectsFromDepartmentInvalidId(){
        UUID randomId = UUID.randomUUID();

        when(departmentRepository.findById(randomId)).thenReturn(Optional.empty());
        assertThrows(DepartmentNotFoundException.class, () -> departmentService.getProjectsByDepartment(randomId));
    }


    @Test
    void testAddProjectToDepartment(){
        Department department = DepartmentTestDataFactory.createDefaultDepartment();
        DepartmentDto departmentDto = DepartmentTestDataFactory.dtoFromDepartment(department);

        Project project = ProjectTestDataFactory.createDefaultProject();
        ProjectDto projectDto = ProjectTestDataFactory.dtoFromProject(project);

        when(departmentRepository.findById(department.getId())).thenReturn(Optional.of(department));
        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));
        when(modelMapper.map(project, ProjectDto.class)).thenReturn(projectDto);

        ProjectDto addedProject = departmentService.addProject(department.getId(), project.getId());

        assertNotNull(addedProject);
        assertEquals(projectDto, addedProject);
        assertEquals(department.getProjects().getFirst(), project);
    }

    @Test
    void testAddProjectToDepartment_ShouldThrowException(){
        Department department = DepartmentTestDataFactory.createDefaultDepartment();
        DepartmentDto departmentDto = DepartmentTestDataFactory.dtoFromDepartment(department);

        Project project = ProjectTestDataFactory.createDefaultProject();
        ProjectDto projectDto = ProjectTestDataFactory.dtoFromProject(project);
        department.getProjects().add(project);

        when(departmentRepository.findById(department.getId())).thenReturn(Optional.of(department));
        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));
        when(modelMapper.map(project, ProjectDto.class)).thenReturn(projectDto);

        assertThrows(DepartmentAlreadyContainsProjectException.class, () -> departmentService.addProject(department.getId(), project.getId()));
    }

    @Test
    void testAddProjectToDepartmentInvalidDepartmentId(){
        UUID randomId = UUID.randomUUID();
        Project project = ProjectTestDataFactory.createDefaultProject();

        when(departmentRepository.findById(randomId)).thenReturn(Optional.empty());
        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));

        assertThrows(DepartmentNotFoundException.class, () -> departmentService.addProject(randomId, project.getId()));
    }


    @Test
    void testAddProjectToDepartmentInvalidProjectId(){
        Department department = DepartmentTestDataFactory.createDefaultDepartment();
        UUID randomId = UUID.randomUUID();

        when(departmentRepository.findById(department.getId())).thenReturn(Optional.of(department));
        when(projectRepository.findById(randomId)).thenReturn(Optional.empty());

        assertThrows(ProjectNotFoundException.class, () -> departmentService.addProject(department.getId(), randomId));
    }

    @Test
    void testDiscardProjectToDepartment(){
        Department department = DepartmentTestDataFactory.createDefaultDepartment();
        DepartmentDto departmentDto = DepartmentTestDataFactory.dtoFromDepartment(department);

        Project project = ProjectTestDataFactory.createDefaultProject();
        ProjectDto projectDto = ProjectTestDataFactory.dtoFromProject(project);
        department.getProjects().add(project);

        when(departmentRepository.findById(department.getId())).thenReturn(Optional.of(department));
        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));
        when(modelMapper.map(project, ProjectDto.class)).thenReturn(projectDto);

        ProjectDto addedProject = departmentService.discardProject(department.getId(), project.getId());

        assertNotNull(addedProject);
        assertEquals(projectDto, addedProject);
        assertEquals(department.getProjects().size(), 0);
    }

    @Test
    void testDiscardProjectToDepartment_ShouldThrowException(){
        Department department = DepartmentTestDataFactory.createDefaultDepartment();
        DepartmentDto departmentDto = DepartmentTestDataFactory.dtoFromDepartment(department);

        Project project = ProjectTestDataFactory.createDefaultProject();
        ProjectDto projectDto = ProjectTestDataFactory.dtoFromProject(project);

        when(departmentRepository.findById(department.getId())).thenReturn(Optional.of(department));
        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));
        when(modelMapper.map(project, ProjectDto.class)).thenReturn(projectDto);

        assertThrows(DepartmentDoesNotContainProjectException.class, () -> departmentService.discardProject(department.getId(), project.getId()) );
    }


    @Test
    void testDiscardProjectToDepartmentInvalidDepartmentId(){
        UUID randomId = UUID.randomUUID();
        Project project = ProjectTestDataFactory.createDefaultProject();

        when(departmentRepository.findById(randomId)).thenReturn(Optional.empty());
        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));

        assertThrows(DepartmentNotFoundException.class, () -> departmentService.discardProject(randomId, project.getId()));
    }


    @Test
    void testDiscardProjectToDepartmentInvalidProjectId(){
        Department department = DepartmentTestDataFactory.createDefaultDepartment();
        UUID randomId = UUID.randomUUID();

        when(departmentRepository.findById(department.getId())).thenReturn(Optional.of(department));
        when(projectRepository.findById(randomId)).thenReturn(Optional.empty());

        assertThrows(ProjectNotFoundException.class, () -> departmentService.discardProject(department.getId(), randomId));
    }

    @Test
    void testAddTeamToDepartment(){
        Department department = DepartmentTestDataFactory.createDefaultDepartment();
        DepartmentDto departmentDto = DepartmentTestDataFactory.dtoFromDepartment(department);

        Team team = TeamTestDataFactory.createDefaultTeam();
        TeamDto teamDto = TeamTestDataFactory.createDtoFromTeam(team);

        when(departmentRepository.findById(department.getId())).thenReturn(Optional.of(department));
        when(teamRepository.findById(team.getId())).thenReturn(Optional.of(team));
        when(modelMapper.map(team, TeamDto.class)).thenReturn(teamDto);

        TeamDto addedTeam = departmentService.addTeam(department.getId(), team.getId());

        assertNotNull(addedTeam);
        assertEquals(teamDto, addedTeam);
        assertEquals(department.getTeams().getFirst(), team);
    }

    @Test
    void testAddTeamToDepartment_ShouldThrowException(){
        Department department = DepartmentTestDataFactory.createDefaultDepartment();
        DepartmentDto departmentDto = DepartmentTestDataFactory.dtoFromDepartment(department);

        Team team = TeamTestDataFactory.createDefaultTeam();
        TeamDto teamDto = TeamTestDataFactory.createDtoFromTeam(team);
        department.getTeams().add(team);

        when(departmentRepository.findById(department.getId())).thenReturn(Optional.of(department));
        when(teamRepository.findById(team.getId())).thenReturn(Optional.of(team));
        when(modelMapper.map(team, TeamDto.class)).thenReturn(teamDto);

        assertThrows(DepartmentAlreadyContainsTeamException.class, () -> departmentService.addTeam(department.getId(), team.getId()));
    }

    @Test
    void testAddTeamToDepartmentInvalidDepartmentId(){
        UUID randomId = UUID.randomUUID();
        Team team = TeamTestDataFactory.createDefaultTeam();

        when(departmentRepository.findById(randomId)).thenReturn(Optional.empty());
        when(teamRepository.findById(team.getId())).thenReturn(Optional.of(team));

        assertThrows(DepartmentNotFoundException.class, () -> departmentService.addTeam(randomId, team.getId()));
    }


    @Test
    void testAddTeamToDepartmentInvalidProjectId(){
        Department department = DepartmentTestDataFactory.createDefaultDepartment();
        UUID randomId = UUID.randomUUID();

        when(departmentRepository.findById(department.getId())).thenReturn(Optional.of(department));
        when(teamRepository.findById(randomId)).thenReturn(Optional.empty());

        assertThrows(TeamNotFoundException.class, () -> departmentService.addTeam(department.getId(), randomId));
    }

    @Test
    void testDiscardTeamToDepartment(){
        Department department = DepartmentTestDataFactory.createDefaultDepartment();
        DepartmentDto departmentDto = DepartmentTestDataFactory.dtoFromDepartment(department);


        Team team = TeamTestDataFactory.createDefaultTeam();
        TeamDto teamDto = TeamTestDataFactory.createDtoFromTeam(team);
        department.getTeams().add(team);

        when(departmentRepository.findById(department.getId())).thenReturn(Optional.of(department));
        when(teamRepository.findById(team.getId())).thenReturn(Optional.of(team));
        when(modelMapper.map(team, TeamDto.class)).thenReturn(teamDto);

        TeamDto discardTeam = departmentService.discardTeam(department.getId(), team.getId());

        assertNotNull(discardTeam);
        assertEquals(teamDto, discardTeam);
        assertFalse(!department.getTeams().contains(team));
    }

    @Test
    void testDiscardTeamToDepartment_ShouldThrowException(){
        Department department = DepartmentTestDataFactory.createDefaultDepartment();
        DepartmentDto departmentDto = DepartmentTestDataFactory.dtoFromDepartment(department);


        Team team = TeamTestDataFactory.createDefaultTeam();
        TeamDto teamDto = TeamTestDataFactory.createDtoFromTeam(team);


        when(departmentRepository.findById(department.getId())).thenReturn(Optional.of(department));
        when(teamRepository.findById(team.getId())).thenReturn(Optional.of(team));
        when(modelMapper.map(team, TeamDto.class)).thenReturn(teamDto);

        assertThrows(DepartmentDoesNotContainTeamException.class, () -> departmentService.discardTeam(department.getId(), team.getId()));
    }

    @Test
    void testDiscardTeamToDepartmentInvalidDepartmentId(){
        UUID randomId = UUID.randomUUID();
        Team team = TeamTestDataFactory.createDefaultTeam();

        when(departmentRepository.findById(randomId)).thenReturn(Optional.empty());
        when(teamRepository.findById(team.getId())).thenReturn(Optional.of(team));

        assertThrows(DepartmentNotFoundException.class, () -> departmentService.discardTeam(randomId, team.getId()));
    }


    @Test
    void testDiscardTeamToDepartmentInvalidProjectId(){
        Department department = DepartmentTestDataFactory.createDefaultDepartment();
        UUID randomId = UUID.randomUUID();

        when(departmentRepository.findById(department.getId())).thenReturn(Optional.of(department));
        when(teamRepository.findById(randomId)).thenReturn(Optional.empty());

        assertThrows(TeamNotFoundException.class, () -> departmentService.discardTeam(department.getId(), randomId));
    }

}
