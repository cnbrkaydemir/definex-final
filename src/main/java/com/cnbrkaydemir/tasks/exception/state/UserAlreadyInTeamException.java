package com.cnbrkaydemir.tasks.exception.state;

import java.util.UUID;

public class UserAlreadyInTeamException extends BaseStateException{
    public UserAlreadyInTeamException(UUID userId, UUID teamId) {
        super("User with id " + userId + " is already a member in team " + teamId);
    }
}
