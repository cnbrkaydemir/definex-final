package com.cnbrkaydemir.tasks.service;

import com.cnbrkaydemir.tasks.dto.UserDto;
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
}
