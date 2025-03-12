package com.informa.tareas.service.impl;

import com.informa.tareas.exception.BusinessRuleException;
import com.informa.tareas.model.dto.TareaInsertRequest;
import com.informa.tareas.model.dto.TareaResponse;
import com.informa.tareas.model.dto.TareaResponseUpdate;
import com.informa.tareas.model.dto.TareaUpdateRequest;
import com.informa.tareas.model.entities.Tarea;
import com.informa.tareas.model.mapper.TareaRequestToTarea;
import com.informa.tareas.model.mapper.TareaToTareaResponse;
import com.informa.tareas.repository.TareaRepository;
import com.informa.tareas.service.TareaService;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
@Slf4j
@Service
public class TareaServiceImpl implements TareaService {
    private final TareaRepository tareaRepository;
    private final TareaRequestToTarea tareaRequestToTarea;
    private final TareaToTareaResponse tareaToTareaResponse;
    private final MessageSource messageSource;
    private final Validator validator;

    @Autowired
    public TareaServiceImpl(TareaRepository tareaRepository, TareaRequestToTarea tareaRequestToTarea,
                            TareaToTareaResponse tareaToTareaResponse, MessageSource messageSource,
                            Validator validator) {
        this.tareaRepository = tareaRepository;
        this.tareaRequestToTarea = tareaRequestToTarea;
        this.tareaToTareaResponse = tareaToTareaResponse;
        this.messageSource = messageSource;
        this.validator = validator;
    }

    @Override
    public Mono<TareaResponse> crear(TareaInsertRequest request, Locale locale) {
        return Mono.just(request)
                .flatMap(tir -> this.validarRequest(tir, locale))
                .flatMap(u -> Mono.just(this.tareaRequestToTarea.mapInsert(u)))
                .flatMap(t -> Mono.fromCallable(()->this.tareaRepository.save(t))
                        .publishOn(Schedulers.boundedElastic())
                        .map(this.tareaToTareaResponse::map));
    }

    @Override
    public Mono<TareaResponse> actualizar(TareaUpdateRequest request, Locale locale) {
        return Mono.just(request)
                .flatMap(tir -> this.validarRequest(tir, locale))
                .flatMap(u -> Mono.just(this.tareaRequestToTarea.mapUpdate(u)))
                .flatMap(t -> Mono.fromCallable(()->this.tareaRepository.save(t))
                .publishOn(Schedulers.boundedElastic())
                .map(this.tareaToTareaResponse::map));
    }

    @Transactional
    @Override
    public Mono<Void> eliminar(Integer idTarea, Locale locale) {
        return Mono.fromRunnable(() -> {
            tareaRepository.findById(idTarea).ifPresent(tareaRepository::delete);
        });
    }

    @Override
    public Mono<Map<String, Object>> listar(String titulo, String descripcion, Short estado, Integer posicionPagina, Integer numeroFilas) {
        Pageable pageable = PageRequest.of((posicionPagina-1), numeroFilas, Sort.by("id").ascending());
        return Mono.fromCallable(() ->tareaRepository.findTareasByFilters(titulo, descripcion, estado, pageable))
                .map(page -> {
                    Map<String, Object> respuesta = new HashMap<>();
                    respuesta.put("tareas", page.getContent().stream().map(this.tareaToTareaResponse::map).toList());
                    respuesta.put("totalPaginas", page.getTotalPages());
                    return respuesta;
                })
                .subscribeOn(Schedulers.boundedElastic()); // Para evitar bloqueo
    }

    @Override
    public Mono<Map<String, Object>> listar(String titulo, String descripcion, Short estado, Long idUsuario, Integer posicionPagina, Integer numeroFilas) {
        Pageable pageable = PageRequest.of((posicionPagina-1), numeroFilas, Sort.by("id").ascending());
        return Mono.fromCallable(() -> tareaRepository.findTareasByFilters(titulo, descripcion, estado, idUsuario, pageable))
                .map(page -> {
                    Map<String, Object> respuesta = new HashMap<>();
                    respuesta.put("tareas", page.getContent().stream().map(this.tareaToTareaResponse::map).toList());
                    respuesta.put("totalPaginas", page.getTotalPages());
                    return respuesta;
                })
                .subscribeOn(Schedulers.boundedElastic()); // Para evitar bloqueo
    }

    @Override
    public Mono<TareaResponseUpdate> obtenerPorId(Integer idTarea) {
        log.info("Iniciando obtenerTareasPorId: ", idTarea);
        return Mono.fromCallable(() -> tareaRepository.findById(idTarea)
                        .map(this.tareaToTareaResponse::mapUpdate))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(Mono::justOrEmpty);
    }
    @Override
    public Flux<TareaResponse> listarPorIdUsuario(Long idUsuario) {
        return Flux.defer(() -> Flux.fromIterable(tareaRepository.findByUsuarioId(idUsuario)))
                .map(this.tareaToTareaResponse::map)
                .subscribeOn(Schedulers.boundedElastic());
    }
    private <T> Mono<T> validarRequest(T tareaUpdateRequest, Locale locale) {
        HashMap<String, String> errores = new HashMap<>();
        var validationResult = validator.validate(tareaUpdateRequest);
        validationResult.forEach(error -> {
            String mensaje = messageSource.getMessage(error.getMessage(), null, locale);
            errores.put(error.getPropertyPath().toString(), mensaje);
        });
        if (!errores.isEmpty()) {
            return Mono.error(new BusinessRuleException(HttpStatus.BAD_REQUEST, errores));
        }
        return Mono.just(tareaUpdateRequest);
    }

}
