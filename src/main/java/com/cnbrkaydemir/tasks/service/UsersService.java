package com.cnbrkaydemir.tasks.service;

import com.cnbrkaydemir.tasks.dto.*;
import com.cnbrkaydemir.tasks.exception.notfound.UserNotFoundException;
import com.cnbrkaydemir.tasks.model.Users;


import java.util.List;
import java.util.UUID;

public interface UsersService {
    UserDto get(UUID id) throws UserNotFoundException;
    List<UserDto> getAll();
    UserDto create(Users user);
    UserDto update(UUID id, UserDto user) throws UserNotFoundException;
    boolean delete(UUID id) throws UserNotFoundException;
    List<TaskDto> getUserTasks(UUID id);
    List<TeamDto> getUserTeams(UUID id);
    List<CommentDto> getUserComments(UUID id);
    List<ProjectDto> getUserProjects(UUID id);

}
