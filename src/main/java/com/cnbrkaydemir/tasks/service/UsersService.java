package com.cnbrkaydemir.tasks.service;

import com.cnbrkaydemir.tasks.dto.CommentDto;
import com.cnbrkaydemir.tasks.dto.TaskDto;
import com.cnbrkaydemir.tasks.dto.TeamDto;
import com.cnbrkaydemir.tasks.dto.UserDto;
import com.cnbrkaydemir.tasks.exception.notfound.UserNotFoundException;


import java.util.List;
import java.util.UUID;

public interface UsersService {
    UserDto get(UUID id) throws UserNotFoundException;
    List<UserDto> getAll();
    UserDto create(UserDto userDto);
    UserDto update(UUID id, UserDto user) throws UserNotFoundException;
    boolean delete(UUID id) throws UserNotFoundException;
    List<TaskDto> getUserTasks(UUID id);
    List<TeamDto> getUserTeams(UUID id);
    List<CommentDto> getUserComments(UUID id);

}
