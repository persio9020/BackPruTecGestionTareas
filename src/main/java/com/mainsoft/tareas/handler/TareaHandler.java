package com.mainsoft.tareas.handler;

import com.mainsoft.tareas.exception.BusinessRuleException;
import com.mainsoft.tareas.model.dto.TareaInsertRequest;
import com.mainsoft.tareas.model.dto.TareaResponse;
import com.mainsoft.tareas.model.dto.TareaUpdateRequest;
import com.mainsoft.tareas.model.dto.UsuarioResponse;
import com.mainsoft.tareas.model.dto.util.Response;
import com.mainsoft.tareas.service.TareaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

@Slf4j
@Component
public class TareaHandler {
    private final TareaService tareaService;

    @Autowired
    public TareaHandler(TareaService tareaService) {
        this.tareaService = tareaService;
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Mono<ServerResponse> crear(ServerRequest request) {
        List<String> acceptLanguaje = request.headers().header(HttpHeaders.ACCEPT_LANGUAGE);
        Locale locale = acceptLanguaje.isEmpty() ? Locale.forLanguageTag("es") : Locale.forLanguageTag(acceptLanguaje.getFirst());

        return request.bodyToMono(TareaInsertRequest.class)
                .flatMap(dto -> tareaService.crear(dto, locale))
                .flatMap(tareaResponse -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(tareaResponse), UsuarioResponse.class)
                )
                .onErrorResume(BusinessRuleException.class, error -> ServerResponse.status(error.getHttpStatus())
                        .body(Mono.just(error.getErrores()), HashMap.class)
                ).onErrorResume(Exception.class, error -> {
                    log.error(error.getMessage(), error);
                    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(Mono.just(new Response(error.getMessage())), Response.class);
                });

    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Mono<ServerResponse> obtenerTareas(ServerRequest request) {
        String titulo = request.queryParam("titulo").orElse("");
        String descripcion = request.queryParam("descripcion").orElse("");
        Short estado = Short.parseShort(request.queryParam("estado").orElse("0"));
        Integer posicionPagina = Integer.parseInt(request.queryParam("posicionPagina").orElse("0"));
        Integer numeroFilas = Integer.parseInt(request.queryParam("numeroFilas").orElse("10"));

        return ServerResponse.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(tareaService.listar(titulo, descripcion, estado, posicionPagina, numeroFilas), TareaResponse.class)
                .switchIfEmpty(ServerResponse.status(HttpStatus.NOT_FOUND).build());
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Mono<ServerResponse> obtenerTareasPorId(ServerRequest request) {
        Locale locale = Locale.forLanguageTag(request.headers().header(HttpHeaders.ACCEPT_LANGUAGE).getFirst());

        return request.bodyToMono(Integer.class)
                .flatMap(id -> tareaService.obtenerPorId(id).flatMap(
                        tr -> ServerResponse
                                .status(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(tr, TareaResponse.class))
                )
                .switchIfEmpty(ServerResponse.status(HttpStatus.NOT_FOUND).build())
                .onErrorResume(Exception.class, this::handleError);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Mono<ServerResponse> actualizar(ServerRequest request) {
        List<String> acceptLanguaje = request.headers().header(HttpHeaders.ACCEPT_LANGUAGE);
        Locale locale = acceptLanguaje.isEmpty() ? Locale.forLanguageTag("es") : Locale.forLanguageTag(acceptLanguaje.getFirst());

        return request.bodyToMono(TareaUpdateRequest.class)
                .flatMap(dto -> tareaService.actualizar(dto, locale))
                .flatMap(tareaResponse -> ServerResponse
                        .status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(tareaResponse), UsuarioResponse.class)
                )
                .onErrorResume(BusinessRuleException.class, error -> ServerResponse.status(error.getHttpStatus())
                        .body(Mono.just(error.getErrores()), HashMap.class)
                ).onErrorResume(Exception.class, error -> {
                    log.error(error.getMessage(), error);
                    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(Mono.just(new Response(error.getMessage())), Response.class);
                });
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Mono<ServerResponse> eliminar(ServerRequest request) {
        List<String> acceptLanguaje = request.headers().header(HttpHeaders.ACCEPT_LANGUAGE);
        Locale locale = acceptLanguaje.isEmpty() ? Locale.forLanguageTag("es") : Locale.forLanguageTag(acceptLanguaje.getFirst());
        Integer id = Integer.parseInt(request.pathVariable("id"));

        return tareaService.eliminar(id, locale)
                .then(ServerResponse.status(HttpStatus.NO_CONTENT).build())
                .switchIfEmpty(ServerResponse.status(HttpStatus.NOT_FOUND).build())
                .onErrorResume(Exception.class, error -> {
                    log.error(error.getMessage(), error);
                    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(Mono.just(new Response(error.getMessage())), Response.class);
                });
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Mono<ServerResponse> obtenerTareasPorIdUsuario(ServerRequest request) {
        String titulo = request.queryParam("titulo").orElse("");
        String descripcion = request.queryParam("descripcion").orElse("");
        Short estado = Short.parseShort(request.queryParam("estado").orElse("0"));
        Integer posicionPagina = Integer.parseInt(request.queryParam("posicionPagina").orElse("0"));
        Integer numeroFilas = Integer.parseInt(request.queryParam("numeroFilas").orElse("10"));
        Long idUsuario = Long.parseLong(request.pathVariable("id"));

        return ServerResponse.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(tareaService.listar(titulo, descripcion, estado, idUsuario, posicionPagina, numeroFilas), TareaResponse.class)
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
