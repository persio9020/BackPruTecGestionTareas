package com.informa.tareas.service.impl;

import com.informa.tareas.model.dto.AuthRequest;
import com.informa.tareas.config.JWTUtil;
import com.informa.tareas.config.PBKDF2Encoder;
import com.informa.tareas.model.dto.AuthResponse;
import com.informa.tareas.model.mapper.UsuarioToUsuarioResponse;
import com.informa.tareas.service.AutenticacionService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
@Service
public class AutenticacionServiceImpl implements AutenticacionService {

    private final UsuarioServiceImpl usuarioService;
    private JWTUtil jwtUtil;
    private PBKDF2Encoder pbkdf2Encoder;
    private UsuarioToUsuarioResponse  usuarioToUsuarioResponse;

    public AutenticacionServiceImpl(UsuarioServiceImpl usuarioService,
                                     PBKDF2Encoder pbkdf2Encoder,
                                    JWTUtil jwtUtil,
                                    UsuarioToUsuarioResponse usuarioToUsuarioResponse ) {
        this.usuarioService = usuarioService;
        this.pbkdf2Encoder = pbkdf2Encoder;
        this.jwtUtil = jwtUtil;
        this.usuarioToUsuarioResponse = usuarioToUsuarioResponse;
    }

    @Override
    public Mono<AuthResponse> login(AuthRequest ar) {
        return usuarioService.findByUsername(ar.getUsername())
                .filter(userDetails -> pbkdf2Encoder.encode(ar.getPassword()).equals(userDetails.getPassword()))
                .flatMap(ud->{
                    AuthResponse aresp = new AuthResponse();
                    aresp.setToken(jwtUtil.generateToken(ud));
                    aresp.setUsuario(usuarioToUsuarioResponse.map(ud));
                    return Mono.just(aresp);
                });
    }
}
