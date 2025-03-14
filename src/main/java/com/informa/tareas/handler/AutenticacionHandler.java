package com.informa.tareas.handler;

import com.informa.tareas.model.dto.AuthRequest;
import com.informa.tareas.model.dto.AuthResponse;
import com.informa.tareas.service.AutenticacionService;
import com.informa.tareas.service.impl.AutenticacionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class AutenticacionHandler {
    private final AutenticacionService autenticacionService;

    @Autowired
    public AutenticacionHandler(AutenticacionServiceImpl autenticacionService) {
        this.autenticacionService = autenticacionService;
    }

    public Mono<ServerResponse> login(ServerRequest request) {
        return request.bodyToMono(AuthRequest.class)
                .flatMap(dto -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                        .body(autenticacionService.login(dto), AuthResponse.class))
                .switchIfEmpty(ServerResponse.status(HttpStatus.UNAUTHORIZED).build());
    }

}
