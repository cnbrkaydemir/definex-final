package com.cnbrkaydemir.tasks.service.impl;

import com.cnbrkaydemir.tasks.dto.ProjectDto;
import com.cnbrkaydemir.tasks.exception.notfound.ProjectNotFoundException;
import com.cnbrkaydemir.tasks.model.Project;
import com.cnbrkaydemir.tasks.repository.ProjectRepository;
import com.cnbrkaydemir.tasks.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    private final ModelMapper modelMapper;

    @Override
    public ProjectDto getProject(UUID id) throws ProjectNotFoundException {
        Project project = projectRepository.findById(id).orElseThrow(() -> new ProjectNotFoundException(id));
        return modelMapper.map(project, ProjectDto.class);
    }

    @Override
    public List<ProjectDto> getProjects() {
        return projectRepository.findAll()
                .stream()
                .map((project -> modelMapper.map(project, ProjectDto.class)))
                .toList();
    }

    @Override
    public ProjectDto createProject(ProjectDto projectDto) {
        Project project = modelMapper.map(projectDto, Project.class);
        project.setDeleted(false);
        return modelMapper.map(projectRepository.save(project), ProjectDto.class);
    }

    @Override
    public boolean deleteProject(UUID id) throws ProjectNotFoundException {
        Project project = projectRepository.findById(id).orElseThrow(() -> new ProjectNotFoundException(id));
        project.setDeleted(true);
        projectRepository.save(project);
        return true;
    }

    @Override
    public ProjectDto updateProject(UUID id, ProjectDto project) throws ProjectNotFoundException {
        Project updatedProject = projectRepository.findById(id).orElseThrow(() -> new ProjectNotFoundException(id));
        modelMapper.map(project, updatedProject);
        return modelMapper.map(projectRepository.save(updatedProject), ProjectDto.class);
    }
}
