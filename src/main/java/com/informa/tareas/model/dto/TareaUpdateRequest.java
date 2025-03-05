package com.informa.tareas.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.sql.Date;

@Data
public class TareaUpdateRequest {
    @NotNull(message = "tarea.id.notblank")
    private Integer id;
    @NotBlank(message = "tarea.titulo.notblank")
    private String titulo;
    @NotBlank(message = "tarea.descripcion.notblank")
    private String descripcion;
    @NotNull(message = "tarea.fechaCreacion.notblank")
    private String fechaCreacion;
    @NotNull(message = "tarea.estado.notblank")
    private Short estadoId;
    @NotNull(message = "tarea.usuario.notblank")
    private Long usuarioId;
}
