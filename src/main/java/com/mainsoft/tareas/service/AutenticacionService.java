package com.mainsoft.tareas.service;

import com.mainsoft.tareas.model.dto.AuthRequest;
import com.mainsoft.tareas.model.dto.AuthResponse;
import reactor.core.publisher.Mono;


public interface AutenticacionService {

  public Mono<AuthResponse> login(AuthRequest ar);
}
