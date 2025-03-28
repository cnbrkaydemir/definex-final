package com.cnbrkaydemir.tasks.service.impl;

import com.cnbrkaydemir.tasks.dto.*;
import com.cnbrkaydemir.tasks.exception.notfound.UserNotFoundException;
import com.cnbrkaydemir.tasks.exception.state.EmailAlreadyExistsException;
import com.cnbrkaydemir.tasks.model.Users;
import com.cnbrkaydemir.tasks.repository.UsersRepository;
import com.cnbrkaydemir.tasks.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = {"user"})
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;

    private final ModelMapper modelMapper;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "user", key = "#id")
    public UserDto get(UUID id) throws UserNotFoundException {
        Users targetUser = usersRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return modelMapper.map(targetUser, UserDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "users")
    public List<UserDto> getAll() {
        return usersRepository.findAll()
                .stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CachePut(value = "user", key = "#result.id")
    @CacheEvict(value = "users", allEntries = true)
    public UserDto create(Users user) {
        if (usersRepository.findByEmail(user.getEmail()).isPresent()) {
          throw new EmailAlreadyExistsException(user.getEmail());
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setDeleted(false);
        Users savedUser = usersRepository.save(user);
        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CachePut(value = "user", key = "#result.id")
    @CacheEvict(value = "users", allEntries = true)
    public UserDto update(UUID id, UserDto user) throws UserNotFoundException {
        Users oldUser = usersRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        modelMapper.map(user, oldUser);
        return modelMapper.map(usersRepository.save(oldUser), UserDto.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "user", key = "#id")
    public boolean delete(UUID id) throws UserNotFoundException {
        Users targetUser = usersRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        targetUser.setDeleted(true);
        usersRepository.save(targetUser);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "tasks", key = "#id")
    public List<TaskDto> getUserTasks(UUID id) {
        Users targetUser = usersRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return usersRepository.findActiveTasksByUserId(targetUser.getId())
                .stream()
                .map(task -> modelMapper.map(task, TaskDto.class))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "teams")
    public List<TeamDto> getUserTeams(UUID id) {
        Users targetUser = usersRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return usersRepository.findActiveTeamsByUserId(targetUser.getId())
                .stream()
                .map(team -> modelMapper.map(team, TeamDto.class))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "comments")
    public List<CommentDto> getUserComments(UUID id) {
        Users targetUser = usersRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return usersRepository.findActiveCommentsByUserId(targetUser.getId())
                .stream()
                .map(comment -> modelMapper.map(comment, CommentDto.class))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "projects")
    public List<ProjectDto> getUserProjects(UUID id) {
        Users targetUser = usersRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return usersRepository.findActiveProjectsByUserId(targetUser.getId())
                .stream()
                .map(project -> modelMapper.map(project, ProjectDto.class))
                .toList();
    }
}
