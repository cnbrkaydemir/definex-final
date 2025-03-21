package com.cnbrkaydemir.tasks.factory;


import com.cnbrkaydemir.tasks.dto.ProjectDto;
import com.cnbrkaydemir.tasks.model.Project;

import java.util.UUID;

public class ProjectTestDataFactory {
    public static Project createDefaultProject(){
        Project project = new Project();
        project.setId(UUID.randomUUID());
        project.setName("Default Project");
        project.setDescription("Default Project Description");
        project.setDepartment(DepartmentTestDataFactory.createDefaultDepartment());
        return project;
    }

    public static Project createCustomProject(String name, String description){
        Project project = new Project();
        project.setId(UUID.randomUUID());
        project.setName(name);
        project.setDescription(description);
        project.setDepartment(DepartmentTestDataFactory.createDefaultDepartment());
        return project;
    }

    public static ProjectDto dtoFromProject(Project project){
        ProjectDto projectDto = new ProjectDto();
        projectDto.setId(project.getId());
        projectDto.setName(project.getName());
        projectDto.setDescription(project.getDescription());
        return projectDto;
    }
}
