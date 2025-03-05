package com.informa.tareas.service;

import com.informa.tareas.model.dto.User;
import com.informa.tareas.model.dto.UsuarioRequest;
import com.informa.tareas.model.dto.UsuarioResponse;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 *
 * @author persi
 */

public interface UsuarioService {

  public Mono<User> findByUsername(String username);

  public Mono<UsuarioResponse> guardarUsuario(UsuarioRequest usuario, Locale locale);

  public Mono<List<Map<String, Object>>> listar();
}
