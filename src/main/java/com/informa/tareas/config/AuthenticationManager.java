package com.informa.tareas.config;

import io.jsonwebtoken.Claims;

import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private final JWTUtil jwtUtil;

    @Autowired
    public AuthenticationManager(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String authToken = authentication.getCredentials().toString();

        return Mono.defer(() -> {
            String username = jwtUtil.getUsernameFromToken(authToken);

            if (!jwtUtil.validateToken(authToken)) {
                return Mono.error(new RuntimeException("Token inválido o expirado"));
            }

            Claims claims = jwtUtil.getAllClaimsFromToken(authToken);
            List<String> rolesMap = claims.get("role", List.class);

            Authentication auth = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    rolesMap.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
            );

            return Mono.just(auth);
        }).onErrorResume(e -> {
            log.error("Error de autenticacion", e);
            return Mono.empty();
        });
    }
}