package com.cnbrkaydemir.tasks.exception.state;

public class DepartmentAlreadyContainsProjectException extends BaseStateException{
    public DepartmentAlreadyContainsProjectException(String departmentName, String projectName) {
        super("Department '" + departmentName + "' already contains project '" + projectName + "'");
    }
}
