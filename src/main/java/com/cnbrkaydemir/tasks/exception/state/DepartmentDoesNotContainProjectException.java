package com.cnbrkaydemir.tasks.exception.state;

public class DepartmentDoesNotContainProjectException extends BaseStateException{
    public DepartmentDoesNotContainProjectException(String departmentName, String teamName) {
        super("Department: " + departmentName + " does not contain project " + teamName);
    }
}
