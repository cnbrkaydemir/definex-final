package com.cnbrkaydemir.tasks.service;

import com.cnbrkaydemir.tasks.dto.LoginDto;
import com.cnbrkaydemir.tasks.dto.UserTokenDto;
import com.cnbrkaydemir.tasks.dto.UserDto;
import org.springframework.security.core.Authentication;

public interface LoginService {

    UserDto findLoggedInUser(Authentication authentication);

    UserTokenDto handleLogin(LoginDto loginDto);
}