package com.cnbrkaydemir.tasks.exception.state;

public class ReasonCannotBeEmptyException extends BaseStateException{
    public ReasonCannotBeEmptyException(String message) {
        super(message);
    }
}
