package com.mainsoft.tareas.service;

import com.mainsoft.tareas.model.dto.TareaInsertRequest;
import com.mainsoft.tareas.model.dto.TareaResponse;
import com.mainsoft.tareas.model.dto.TareaUpdateRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Locale;

public interface TareaService {
    public Mono<TareaResponse> crear(TareaInsertRequest request, Locale locale);
    public Mono<TareaResponse> actualizar(TareaUpdateRequest request, Locale locale);
    public Mono<Void> eliminar(Integer id, Locale locale);
    public Flux<TareaResponse> listar(String titulo, String descripcion, Short estado, Long idUsuario, Integer posicionPagina, Integer numeroFilas);
    public Flux<TareaResponse> listar(String titulo, String descripcion, Short estado, Integer posicionPagina, Integer numeroFilas);
    public Mono<TareaResponse> obtenerPorId(Integer idTarea);
    public Flux<TareaResponse> listarPorIdUsuario(Long idUsuario);
}
