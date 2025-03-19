package com.cnbrkaydemir.tasks.service.impl;

import com.cnbrkaydemir.tasks.dto.LoginDto;
import com.cnbrkaydemir.tasks.dto.UserDto;
import com.cnbrkaydemir.tasks.dto.UserTokenDto;
import com.cnbrkaydemir.tasks.exception.notfound.UserEmailNotFoundException;
import com.cnbrkaydemir.tasks.repository.UsersRepository;
import com.cnbrkaydemir.tasks.service.LoginService;
import com.cnbrkaydemir.tasks.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final ModelMapper modelMapper;

    private final UsersRepository usersRepository;

    private final TokenService tokenService;

    private final AuthenticationManager authenticationManager;

    @Override
    public UserDto findLoggedInUser(Authentication authentication) {
        return modelMapper.map(usersRepository.findByEmail(authentication.getName())
                        .orElseThrow(() -> new UserEmailNotFoundException(authentication.getName()))
                , UserDto.class);
    }

    @Override
    public UserTokenDto handleLogin(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
        );
        String token = tokenService.generateJwt(authentication);
        UserDto  loggedIn = this.findLoggedInUser(authentication);
        return UserTokenDto.of(loggedIn, token);
    }
}
