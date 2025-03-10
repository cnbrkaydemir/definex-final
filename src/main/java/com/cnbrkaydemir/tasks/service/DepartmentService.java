package com.cnbrkaydemir.tasks.service;

import com.cnbrkaydemir.tasks.dto.DepartmentDto;
import com.cnbrkaydemir.tasks.model.Department;

import java.util.List;
import java.util.UUID;

public interface DepartmentService {
    DepartmentDto getDepartmentById(UUID id);
    List<DepartmentDto> getAllDepartments();
    DepartmentDto createDepartment(Department department);
    DepartmentDto updateDepartment(UUID id, DepartmentDto department);
    boolean deleteDepartment(UUID id);
}
