package com.mainsoft.tareas.service.impl;

import com.mainsoft.tareas.config.PBKDF2Encoder;
import com.mainsoft.tareas.model.dto.util.Role;
import com.mainsoft.tareas.config.User;
import com.mainsoft.tareas.exception.BusinessRuleException;
import com.mainsoft.tareas.model.dto.UsuarioRequest;
import com.mainsoft.tareas.model.dto.UsuarioResponse;
import com.mainsoft.tareas.model.entities.Usuario;
import com.mainsoft.tareas.model.entities.UsuarioRol;
import com.mainsoft.tareas.model.mapper.UsuarioRequestToUsuario;
import com.mainsoft.tareas.model.mapper.UsuarioToUsuarioResponse;
import com.mainsoft.tareas.repository.UsuarioRepository;
import com.mainsoft.tareas.repository.UsuarioRolRepository;
import com.mainsoft.tareas.service.UsuarioService;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * @author Hector Leon
 */
@Service
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
        Usuario usuario = this.usuarioRepository.findByNombre(username);
        User userJWT = new User();
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
    @Transactional
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
