package com.mainsoft.tareas.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.sql.Date;

@Data
public class TareaInsertRequest {
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
