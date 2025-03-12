package com.cnbrkaydemir.tasks.exception.notfound;

import java.util.UUID;

public class DepartmentNotFoundException extends NotFoundException {
    public DepartmentNotFoundException(UUID departmentId) {
        super("Department with id " + departmentId + " not found");
    }
}
