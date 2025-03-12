package com.cnbrkaydemir.tasks.exception.notfound;

import java.util.UUID;

public class TeamNotFoundException extends NotFoundException {
    public TeamNotFoundException(UUID teamId) {
        super("Team with id " + teamId + " not found");
    }
}
