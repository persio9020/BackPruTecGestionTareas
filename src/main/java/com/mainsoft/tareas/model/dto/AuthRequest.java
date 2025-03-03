package com.mainsoft.tareas.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Datos de ingreso del usuario")
@Data
public class AuthRequest {
    @Schema(example = "admin", description = "Nombre de usuario")
    private String username;
    @Schema(example = "*****", description = "Contraseña")
    private String password;
}
