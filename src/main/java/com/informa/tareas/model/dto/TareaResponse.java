package com.informa.tareas.model.dto;

import lombok.Data;

import java.sql.Date;
@Data
public class TareaResponse {
    private Integer id;
    private String titulo;
    private String descripcion;
    private String fechaCreacion;
    private String nombreEstado;
    private String nombreUsuario;
}
