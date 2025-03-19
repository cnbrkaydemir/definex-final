package com.cnbrkaydemir.tasks.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

public interface TokenService {

    String generateJwt(Authentication auth);

    List<String> populateAuthorities(Collection<? extends GrantedAuthority> collection);
}