package com.informa.tareas.service;

import com.informa.tareas.model.dto.AuthRequest;
import com.informa.tareas.model.dto.AuthResponse;
import reactor.core.publisher.Mono;


public interface AutenticacionService {

  public Mono<AuthResponse> login(AuthRequest ar);
}
