package com.cnbrkaydemir.tasks.exception.state;

public class DepartmentDoesNotContainTeamException extends BaseStateException {
    public DepartmentDoesNotContainTeamException(String departmentName, String teamName) {
        super("Department " + departmentName + " does not contain team " + teamName);
    }
}
