package com.cnbrkaydemir.tasks.exception.notfound;

import java.util.UUID;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(UUID userId)
    {
        super("User with ID " + userId + " not found.");
    }
}
