package com.cnbrkaydemir.tasks.exception.state;

public class EmailAlreadyExistsException extends BaseStateException{
    public EmailAlreadyExistsException(String email) {
        super("User with email: "+email+" already exists");
    }

}
