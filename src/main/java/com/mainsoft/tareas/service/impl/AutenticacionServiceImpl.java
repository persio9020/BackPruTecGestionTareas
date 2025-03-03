package com.mainsoft.tareas.service.impl;

import com.mainsoft.tareas.model.dto.AuthRequest;
import com.mainsoft.tareas.config.JWTUtil;
import com.mainsoft.tareas.config.PBKDF2Encoder;
import com.mainsoft.tareas.model.dto.AuthResponse;
import com.mainsoft.tareas.service.AutenticacionService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
@Service
public class AutenticacionServiceImpl implements AutenticacionService {

    private final UsuarioServiceImpl usuarioService;
    private JWTUtil jwtUtil;
    private PBKDF2Encoder pbkdf2Encoder;

    public AutenticacionServiceImpl(UsuarioServiceImpl usuarioService,
                                     PBKDF2Encoder pbkdf2Encoder,
                                    JWTUtil jwtUtil ) {
        this.usuarioService = usuarioService;
        this.pbkdf2Encoder = pbkdf2Encoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<AuthResponse> login(AuthRequest ar) {
        return usuarioService.findByUsername(ar.getUsername())
                .filter(userDetails -> pbkdf2Encoder.encode(ar.getPassword()).equals(userDetails.getPassword()))
                .map(userDetails -> new AuthResponse(jwtUtil.generateToken(userDetails)));
    }
}
