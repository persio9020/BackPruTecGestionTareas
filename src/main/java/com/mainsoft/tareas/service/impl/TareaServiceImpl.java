package com.mainsoft.tareas.service.impl;

import com.mainsoft.tareas.exception.BusinessRuleException;
import com.mainsoft.tareas.model.dto.TareaInsertRequest;
import com.mainsoft.tareas.model.dto.TareaResponse;
import com.mainsoft.tareas.model.dto.TareaUpdateRequest;
import com.mainsoft.tareas.model.mapper.TareaRequestToTarea;
import com.mainsoft.tareas.model.mapper.TareaToTareaResponse;
import com.mainsoft.tareas.repository.TareaRepository;
import com.mainsoft.tareas.service.TareaService;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Locale;

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

    @Transactional
    @Override
    public Mono<TareaResponse> crear(TareaInsertRequest request, Locale locale) {
        return Mono.just(request)
                .flatMap(tir -> this.validarRequest(tir, locale))
                .flatMap(u -> Mono.just(this.tareaRequestToTarea.mapInsert(u)))
                .flatMap(t -> Mono.just(this.tareaRepository.save(t))
                        .map(this.tareaToTareaResponse::map));
    }

    @Transactional
    @Override
    public Mono<TareaResponse> actualizar(TareaUpdateRequest request, Locale locale) {
        return Mono.just(request)
                .flatMap(tir -> this.validarRequest(tir, locale))
                .flatMap(u -> Mono.just(this.tareaRequestToTarea.mapUpdate(u)))
                .flatMap(t -> Mono.just(this.tareaRepository.save(t)))
                .map(this.tareaToTareaResponse::map);
    }

    @Transactional
    @Override
    public Mono<Void> eliminar(Integer idTarea, Locale locale) {
        return Mono.fromRunnable(() -> {
            tareaRepository.findById(idTarea).ifPresent(tareaRepository::delete);
        });
    }

    @Override
    public Flux<TareaResponse> listar(String titulo, String descripcion, Short estado, Integer posicionPagina, Integer numeroFilas) {
        Pageable pageable = PageRequest.of(posicionPagina, numeroFilas, Sort.by("id").ascending());
        return Flux.fromIterable(tareaRepository.findTareasByFilters(titulo, descripcion, estado, pageable)
                .map(this.tareaToTareaResponse::map));
    }

    @Override
    public Flux<TareaResponse> listar(String titulo, String descripcion, Short estado, Long idUsuario, Integer posicionPagina, Integer numeroFilas) {
        Pageable pageable = PageRequest.of(posicionPagina, numeroFilas, Sort.by("id").ascending());
        return Flux.fromIterable(tareaRepository.findTareasByFilters(titulo, descripcion, estado, idUsuario, pageable)
                .map(this.tareaToTareaResponse::map));
    }

    @Override
    public Mono<TareaResponse> obtenerPorId(Integer idTarea) {
        return Mono.fromCallable(() -> tareaRepository.findById(idTarea)
                        .map(this.tareaToTareaResponse::map))
                .flatMap(Mono::justOrEmpty);
    }

    @Override
    public Flux<TareaResponse> listarPorIdUsuario(Long idUsuario) {
        return Flux.fromIterable(tareaRepository.findByUsuarioId(idUsuario))
                .map(this.tareaToTareaResponse::map);
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
