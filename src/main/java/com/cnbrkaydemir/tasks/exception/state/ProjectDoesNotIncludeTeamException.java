package com.cnbrkaydemir.tasks.exception.state;

public class ProjectDoesNotIncludeTeamException extends BaseStateException {
    public ProjectDoesNotIncludeTeamException() {
        super("The corresponding team is not part of the project");
    }
}
