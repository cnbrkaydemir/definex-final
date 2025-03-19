package com.cnbrkaydemir.tasks.exception.state;

public class TaskAlreadyAssignedException extends BaseStateException{
    public TaskAlreadyAssignedException(String taskName) {
        super("Task: "+taskName+" is already assigned.");
    }
}
