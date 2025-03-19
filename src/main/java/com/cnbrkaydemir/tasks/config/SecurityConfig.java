package com.cnbrkaydemir.tasks.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import com.nimbusds.jose.jwk.RSAKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers( HttpMethod.POST,"/api/department/**").hasAnyAuthority("ADMIN", "PROJECT_MANAGER")
                        .requestMatchers( HttpMethod.PATCH,"/api/department/**").hasAnyAuthority("ADMIN", "PROJECT_MANAGER")
                        .requestMatchers(HttpMethod.DELETE,"/api/department/**").hasAnyAuthority("ADMIN", "PROJECT_MANAGER")
                        .requestMatchers(HttpMethod.GET,"/api/department/**").authenticated()

                        .requestMatchers(HttpMethod.POST, "/api/project/**").hasAnyAuthority("ADMIN", "PROJECT_MANAGER")
                        .requestMatchers(HttpMethod.DELETE, "/api/project/**").hasAnyAuthority("ADMIN", "PROJECT_MANAGER")
                        .requestMatchers(HttpMethod.PATCH, "/api/project/**").hasAnyAuthority("ADMIN", "PROJECT_MANAGER")
                        .requestMatchers(HttpMethod.GET,"/api/project/**").authenticated()

                        .requestMatchers(HttpMethod.POST, "/api/team/**").hasAnyAuthority("ADMIN", "PROJECT_MANAGER", "TEAM_LEADER")
                        .requestMatchers(HttpMethod.PATCH, "/api/team/**").hasAnyAuthority("ADMIN", "PROJECT_MANAGER", "TEAM_LEADER")
                        .requestMatchers(HttpMethod.DELETE, "/api/team/**").hasAnyAuthority("ADMIN", "PROJECT_MANAGER")
                        .requestMatchers(HttpMethod.GET,"/api/team/**").authenticated()

                        .requestMatchers(HttpMethod.POST, "/api/task/v1").hasAnyAuthority("ADMIN", "PROJECT_MANAGER")
                        .requestMatchers(HttpMethod.PATCH, "/api/task/v1/{taskId}").hasAnyAuthority("ADMIN", "PROJECT_MANAGER", "TEAM_LEADER")
                        .requestMatchers(HttpMethod.PATCH, "/api/task/v1/{taskId}/priority").hasAnyAuthority("ADMIN", "PROJECT_MANAGER", "TEAM_LEADER")
                        .requestMatchers(HttpMethod.PATCH, "/api/task/v1/{taskId}/progress").hasAnyAuthority("ADMIN", "PROJECT_MANAGER", "TEAM_LEADER", "TEAM_MEMBER")
                        .requestMatchers(HttpMethod.DELETE, "/api/task/**").hasAnyAuthority("ADMIN", "PROJECT_MANAGER")
                        .requestMatchers(HttpMethod.GET,"/api/task/**").authenticated()

                        .requestMatchers(HttpMethod.POST, "/api/user/**").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/api/user/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/user/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/user/**").authenticated()

                        .requestMatchers("/api/comment/**").authenticated()

                        .requestMatchers("/api/comment/**").authenticated()

                        .requestMatchers("/api/login/**").permitAll()

                )
                .csrf(AbstractHttpConfigurer::disable)
                .oauth2ResourceServer(oauth ->
                        oauth.jwt(jwtConfigurer ->
                                jwtConfigurer.jwtAuthenticationConverter(jwtAuthenticationConverter())))
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
                                .accessDeniedHandler(new BearerTokenAccessDeniedHandler()));

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName("authorities");
        grantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    @Bean
    public KeyPair keyPair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            return keyPairGenerator.generateKeyPair();
        } catch (Exception e) {
            throw new RuntimeException("error generating key pair", e);
        }
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        KeyPair keypair = keyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keypair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keypair.getPrivate();

        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();

        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey((RSAPublicKey) keyPair().getPublic()).build();
    }

    @Bean
    public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }
}
