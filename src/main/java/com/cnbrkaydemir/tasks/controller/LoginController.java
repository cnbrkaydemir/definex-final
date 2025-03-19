package com.cnbrkaydemir.tasks.controller;

import com.cnbrkaydemir.tasks.dto.LoginDto;
import com.cnbrkaydemir.tasks.dto.UserTokenDto;
import com.cnbrkaydemir.tasks.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/token")
    public ResponseEntity<UserTokenDto> token(@RequestBody LoginDto loginDto) {
        return ResponseEntity.ok(loginService.handleLogin(loginDto));
    }

}
