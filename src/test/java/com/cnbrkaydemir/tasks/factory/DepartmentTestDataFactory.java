package com.cnbrkaydemir.tasks.factory;

import com.cnbrkaydemir.tasks.dto.DepartmentDto;
import com.cnbrkaydemir.tasks.model.Department;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DepartmentTestDataFactory {

    public static Department createDefaultDepartment() {
        Department department = new Department();
        department.setName("Default Department");
        department.setId(UUID.randomUUID());
        department.setDescription("Default Department Description");
        department.setProjects(new ArrayList<>());
        department.setTeams(new ArrayList<>());
        return department;
    }

    public static Department createCustomDepartment(String name, String description) {
        Department department = new Department();
        department.setName(name);
        department.setDescription(description);
        department.setId(UUID.randomUUID());
        return department;
    }

    public static DepartmentDto dtoFromDepartment(Department department) {
        DepartmentDto dto = new DepartmentDto();
        dto.setId(department.getId());
        dto.setName(department.getName());
        dto.setDescription(department.getDescription());
        return dto;
    }
}
