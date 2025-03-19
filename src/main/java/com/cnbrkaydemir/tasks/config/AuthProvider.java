package com.cnbrkaydemir.tasks.config;

import com.cnbrkaydemir.tasks.model.UserRole;
import com.cnbrkaydemir.tasks.model.Users;
import com.cnbrkaydemir.tasks.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthProvider implements AuthenticationProvider {

    private final UsersRepository usersRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String pwd = authentication.getCredentials().toString();

        Users targetUser = usersRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("User not found"));

        String userPassword = targetUser.getPassword();
        if(passwordEncoder.matches(pwd, userPassword)) {
            return new UsernamePasswordAuthenticationToken(email, pwd,
                    getGrantedAuthorities(targetUser.getRole()));
        }

        throw new BadCredentialsException("Invalid password!");
    }

    private List<GrantedAuthority> getGrantedAuthorities(UserRole role) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        grantedAuthorities.add(new SimpleGrantedAuthority(role.toString()));

        return grantedAuthorities;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(List.of(this));
    }
}
