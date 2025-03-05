package com.informa.tareas.model;

import com.informa.tareas.model.dto.util.Role;
import lombok.Data;

import java.util.List;

@Data
public class AuthUsuarioResponse {
    private Long id;
    private String username;
    private List<Role> roles;
}
