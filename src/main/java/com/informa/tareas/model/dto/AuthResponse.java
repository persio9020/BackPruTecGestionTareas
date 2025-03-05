package com.informa.tareas.model.dto;

import com.informa.tareas.model.AuthUsuarioResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String token;
    private AuthUsuarioResponse usuario;
}
