package com.cnbrkaydemir.tasks.exception.notfound;

public class UserEmailNotFoundException extends NotFoundException {
    public UserEmailNotFoundException(String email) {
        super("Cannot find user email : "+ email);
    }
}
