package com.informa.tareas.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@Schema(description = "Datos de ingreso del usuario")
@ToString
public class UsuarioRequest {
    @NotBlank(message = "{usuario.nombre.notblank}")
    private String nombre;
    @NotBlank(message = "{usuario.contrasenia.notblank}")
    private String contrasenia;
    @NotBlank(message = "{usuario.correo.notblank}")
    private String correo;
    @NotEmpty(message = "{usuario.rol.notempty}")
    private List<@NotNull RolRequest> roles;
}
