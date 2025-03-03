package com.mainsoft.tareas.model.dto;

import lombok.Data;

@Data
public class UsuarioResponse {
    private Long id;
    private String nombre;
    private String contrasenia;
    private String correo;
}
