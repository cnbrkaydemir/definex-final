package com.cnbrkaydemir.tasks.exception.state;

import com.cnbrkaydemir.tasks.model.Team;
import com.cnbrkaydemir.tasks.model.Users;

public class UserNotInTeamException extends BaseStateException {
    public UserNotInTeamException(Users user, Team team) {
        super(user.getFirstName()+" "+user.getLastName()+ " is not a part of team : "+team.getName());
    }
}
