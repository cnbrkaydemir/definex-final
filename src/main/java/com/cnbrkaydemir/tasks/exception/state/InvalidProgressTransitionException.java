package com.cnbrkaydemir.tasks.exception.state;

public class InvalidProgressTransitionException extends BaseStateException{
    public InvalidProgressTransitionException(String message) {
        super(message);
    }
}
