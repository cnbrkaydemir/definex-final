package com.cnbrkaydemir.tasks.exception.notfound;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(int userId)
    {
        super("User with ID " + userId + " not found.");
    }
}
