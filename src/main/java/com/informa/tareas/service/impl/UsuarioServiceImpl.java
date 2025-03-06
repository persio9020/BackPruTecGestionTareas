package com.informa.tareas.service.impl;

import com.informa.tareas.config.PBKDF2Encoder;
import com.informa.tareas.model.dto.util.Role;
import com.informa.tareas.model.dto.User;
import com.informa.tareas.exception.BusinessRuleException;
import com.informa.tareas.model.dto.UsuarioRequest;
import com.informa.tareas.model.dto.UsuarioResponse;
import com.informa.tareas.model.entities.Usuario;
import com.informa.tareas.model.entities.UsuarioRol;
import com.informa.tareas.model.mapper.UsuarioRequestToUsuario;
import com.informa.tareas.model.mapper.UsuarioToUsuarioResponse;
import com.informa.tareas.repository.UsuarioRepository;
import com.informa.tareas.repository.UsuarioRolRepository;
import com.informa.tareas.service.UsuarioService;
import jakarta.validation.Validator;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import reactor.core.scheduler.Schedulers;

import java.util.*;

/**
 * @author Hector Leon
 */
@Service
@Slf4j
public class UsuarioServiceImpl implements UsuarioService {
    private final UsuarioToUsuarioResponse usuarioToUsuarioResponse;
    private final UsuarioRequestToUsuario usuarioRequestToUsuario;
    private final UsuarioRepository usuarioRepository;
    private PBKDF2Encoder pbkdf2Encoder;
    private final Validator validator;
    private final MessageSource messageSource;
    private final UsuarioRolRepository usuarioRolRepository;

    @Autowired
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository,
                              UsuarioToUsuarioResponse usuarioToUsuarioResponse,
                              UsuarioRequestToUsuario usuarioRequestToUsuario,
                              PBKDF2Encoder pbkdf2Encoder,
                              Validator validator,
                              MessageSource messageSource,
                              UsuarioRolRepository usuarioRolRepository) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioToUsuarioResponse = usuarioToUsuarioResponse;
        this.usuarioRequestToUsuario = usuarioRequestToUsuario;
        this.pbkdf2Encoder = pbkdf2Encoder;
        this.validator = validator;
        this.messageSource = messageSource;
        this.usuarioRolRepository = usuarioRolRepository;
    }


    @Override
    public Mono<User> findByUsername(String username) {
        List<Usuario> listUsuario = this.usuarioRepository.findByNombre(username);
        Usuario usuario = listUsuario.getLast();
        User userJWT = new User();
        userJWT.setId(usuario.getId());
        userJWT.setUsername(usuario.getNombre());
        userJWT.setPassword(usuario.getContrasenia());
        List<Role> listRoles = new ArrayList<>();
        for (UsuarioRol ur : usuario.getUsuarioRolList()) {
            System.out.println(ur.getId());
            if (ur.getRol().getNombre().equals(Role.ROLE_ADMIN.name())) {
                listRoles.add(Role.ROLE_ADMIN);
            }
            if (ur.getRol().getNombre().equals(Role.ROLE_USER.name())) {
                listRoles.add(Role.ROLE_USER);
            }
        }
        userJWT.setRoles(listRoles);
        return Mono.just(userJWT);
    }

    @Override
    public Mono<UsuarioResponse> guardarUsuario(UsuarioRequest usuario, Locale locale) {
        return Mono.just(usuario)
                .map(u -> this.validarRequest(u, locale))
                .map(u -> this.usuarioRequestToUsuario.map(u))
                .flatMap(u -> {
                    u.setContrasenia(pbkdf2Encoder.encode(u.getContrasenia()));
                    return Mono.just(usuarioRepository.save(u));
                })
                .flatMap(usuIn -> Flux.fromIterable(usuario.getRoles())
                        .flatMap(r -> Mono.fromRunnable(() -> this.usuarioRolRepository.insertarUsuarioRol(usuIn.getId(), r.getId())))
                        .then(Mono.just(usuIn)))
                .map(this.usuarioToUsuarioResponse::map);
    }

    @Override
    public Mono<List<Map<String, Object>>> listar() {
        return Mono.fromCallable(() -> {
            List<Map<String, Object>> resultado = new ArrayList<>();
            usuarioRepository.findAll().forEach(usuario -> {
                Map<String, Object> respuesta = new HashMap<>();
                respuesta.put("id", usuario.getId());
                respuesta.put("nombre", usuario.getNombre());
                resultado.add(respuesta);
            });
            return resultado;
        }).subscribeOn(Schedulers.boundedElastic());
    }

    private <T> T validarRequest(T tareaUpdateRequest, Locale locale) {
        var errores = new HashMap<String, String>();
        var validationResult = validator.validate(tareaUpdateRequest);
        validationResult.forEach(error -> {
            String mensaje = messageSource.getMessage(error.getMessage(), null, locale);
            errores.put(error.getPropertyPath().toString(), mensaje);
        });
        if (!errores.isEmpty()) {
            throw new BusinessRuleException(HttpStatus.BAD_REQUEST, errores);
        }
        return tareaUpdateRequest;
    }
}
