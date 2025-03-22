package com.cnbrkaydemir.tasks.exception.state;

public class ProjectAlreadyContainsTeamException extends BaseStateException {
    public ProjectAlreadyContainsTeamException(String projectName, String teamName) {
        super("Project : "+projectName+" already contains team : "+teamName);
    }
}
