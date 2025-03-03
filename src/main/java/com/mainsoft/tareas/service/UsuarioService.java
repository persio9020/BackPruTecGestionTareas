package com.mainsoft.tareas.service;

import com.mainsoft.tareas.config.User;
import com.mainsoft.tareas.model.dto.UsuarioRequest;
import com.mainsoft.tareas.model.dto.UsuarioResponse;
import reactor.core.publisher.Mono;

import java.util.Locale;

/**
 *
 * @author persi
 */

public interface UsuarioService {

  public Mono<User> findByUsername(String username);

  public Mono<UsuarioResponse> guardarUsuario(UsuarioRequest usuario, Locale locale);

}
