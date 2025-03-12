package com.cnbrkaydemir.tasks.service.impl;

import com.cnbrkaydemir.tasks.dto.UserDto;
import com.cnbrkaydemir.tasks.exception.notfound.UserNotFoundException;
import com.cnbrkaydemir.tasks.model.Users;
import com.cnbrkaydemir.tasks.repository.UsersRepository;
import com.cnbrkaydemir.tasks.service.UsersService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;

    private final ModelMapper modelMapper;

    @Override
    public UserDto get(UUID id) throws UserNotFoundException {
        Users targetUser = usersRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return modelMapper.map(targetUser, UserDto.class);
    }

    @Override
    public List<UserDto> getAll() {
        return usersRepository.findAll()
                .stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .toList();
    }

    @Override
    public UserDto create(Users user) {
        Users newUser = usersRepository.save(user);
        return modelMapper.map(newUser, UserDto.class);
    }

    @Override
    public UserDto update(UUID id, UserDto user) throws UserNotFoundException {
        Users oldUser = usersRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        modelMapper.map(user, oldUser);
        return modelMapper.map(usersRepository.save(oldUser), UserDto.class);
    }

    @Override
    public boolean delete(UUID id) throws UserNotFoundException {
        Users targetUser = usersRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        usersRepository.delete(targetUser);
        return true;
    }
}
