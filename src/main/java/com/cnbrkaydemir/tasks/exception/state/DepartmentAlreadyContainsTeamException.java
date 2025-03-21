package com.cnbrkaydemir.tasks.exception.state;

public class DepartmentAlreadyContainsTeamException extends BaseStateException {
    public DepartmentAlreadyContainsTeamException(String departmentName, String teamName) {
        super("Department: " + departmentName + " already contains team: " + teamName);
    }
}
