package com.informa.tareas.model.dto;

import lombok.Data;

@Data
public class TareaResponseUpdate {
    private Integer id;
    private String titulo;
    private String descripcion;
    private String fechaCreacion;
    private String estadoId;
    private String usuarioId;
}
