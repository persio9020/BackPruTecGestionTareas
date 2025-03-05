package com.informa.tareas.service;

import com.informa.tareas.model.dto.TareaInsertRequest;
import com.informa.tareas.model.dto.TareaResponse;
import com.informa.tareas.model.dto.TareaResponseUpdate;
import com.informa.tareas.model.dto.TareaUpdateRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public interface TareaService {
    public Mono<TareaResponse> crear(TareaInsertRequest request, Locale locale);
    public Mono<TareaResponse> actualizar(TareaUpdateRequest request, Locale locale);
    public Mono<Void> eliminar(Integer id, Locale locale);
    public Mono<Map<String, Object>> listar(String titulo, String descripcion, Short estado, Long idUsuario, Integer posicionPagina, Integer numeroFilas);
    public Mono<Map<String, Object>> listar(String titulo, String descripcion, Short estado, Integer posicionPagina, Integer numeroFilas);
    public Mono<TareaResponseUpdate> obtenerPorId(Integer idTarea);
    public Flux<TareaResponse> listarPorIdUsuario(Long idUsuario);
}
