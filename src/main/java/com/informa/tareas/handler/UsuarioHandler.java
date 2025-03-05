package com.informa.tareas.handler;

import com.informa.tareas.exception.BusinessRuleException;
import com.informa.tareas.model.dto.TareaResponse;
import com.informa.tareas.model.dto.UsuarioRequest;
import com.informa.tareas.model.dto.UsuarioResponse;
import com.informa.tareas.model.dto.util.Response;
import com.informa.tareas.service.UsuarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


@Slf4j
@Component
public class UsuarioHandler {
    private final UsuarioService usuarioService;
    private final MessageSource messageSource;

    @Autowired
    public UsuarioHandler(UsuarioService usuarioService, MessageSource messageSource) {
        this.usuarioService = usuarioService;
        this.messageSource = messageSource;
    }

    public Mono<ServerResponse> registrarUsuario(ServerRequest request) {
        List<String> acceptLanguaje = request.headers().header(HttpHeaders.ACCEPT_LANGUAGE);
        Locale locale = acceptLanguaje.isEmpty() ? Locale.forLanguageTag("es") : Locale.forLanguageTag(acceptLanguaje.getFirst());

        return request.bodyToMono(UsuarioRequest.class).flatMap(usuarioRequest ->
                        usuarioService.guardarUsuario(usuarioRequest, locale)
                ).flatMap(usuarioResponse -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(usuarioResponse), UsuarioResponse.class))
                .onErrorResume(BusinessRuleException.class, error -> ServerResponse.status(error.getHttpStatus())
                        .body(Mono.just(error.getErrores()), HashMap.class)
                ).onErrorResume(Exception.class, error -> {
                    log.error("Error al registrar usuario", error);
                    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(Mono.just(new Response(error.getMessage())), Response.class);
                });
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Mono<ServerResponse> listar(ServerRequest request) {
        return ServerResponse.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(usuarioService.listar(), ArrayList.class)
                .switchIfEmpty(ServerResponse.status(HttpStatus.NOT_FOUND).build());
    }

    public Mono<ServerResponse> handleError(Exception error) {
        if (error instanceof BusinessRuleException bre) {
            return ServerResponse.status(bre.getHttpStatus())
                    .body(bre.getErrores(), HashMap.class);
        }
        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new Response(error.getMessage()), Response.class);
    }

}