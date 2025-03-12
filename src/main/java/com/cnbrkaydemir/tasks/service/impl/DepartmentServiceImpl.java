package com.cnbrkaydemir.tasks.service.impl;

import com.cnbrkaydemir.tasks.dto.DepartmentDto;
import com.cnbrkaydemir.tasks.exception.notfound.DepartmentNotFoundException;
import com.cnbrkaydemir.tasks.model.Department;
import com.cnbrkaydemir.tasks.repository.DepartmentRepository;
import com.cnbrkaydemir.tasks.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    private final ModelMapper modelMapper;

    @Override
    public DepartmentDto getDepartmentById(UUID id) throws DepartmentNotFoundException {
        Department targetDepartment = departmentRepository.findById(id).orElseThrow(() -> new DepartmentNotFoundException(id));
        return modelMapper.map(targetDepartment, DepartmentDto.class);
    }

    @Override
    public List<DepartmentDto> getAllDepartments() {
        return departmentRepository.findAll()
                .stream()
                .map((department)-> modelMapper.map(department, DepartmentDto.class))
                .toList();
    }

    @Override
    public DepartmentDto createDepartment(Department department) {
        return modelMapper.map(departmentRepository.save(department), DepartmentDto.class);
    }

    @Override
    public DepartmentDto updateDepartment(UUID id, DepartmentDto department) throws DepartmentNotFoundException {
        Department oldDepartment = departmentRepository.findById(id).orElseThrow(() -> new DepartmentNotFoundException(id));
        modelMapper.map(department, oldDepartment);
        return modelMapper.map(departmentRepository.save(oldDepartment), DepartmentDto.class);
    }

    @Override
    public boolean deleteDepartment(UUID id) throws DepartmentNotFoundException {
        Department targetDepartment = departmentRepository.findById(id).orElseThrow(() -> new DepartmentNotFoundException(id));
        targetDepartment.setDeleted(true);
        departmentRepository.save(targetDepartment);
        return true;
    }
}
