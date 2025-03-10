package com.cnbrkaydemir.tasks.service;

import com.cnbrkaydemir.tasks.dto.UserDto;
import com.cnbrkaydemir.tasks.model.Users;

import java.util.List;
import java.util.UUID;

public interface UsersService {
    UserDto get(UUID id);
    List<UserDto> getAll();
    UserDto create(Users user);
    UserDto update(UUID id, UserDto user);
    boolean delete(UUID id);
}
