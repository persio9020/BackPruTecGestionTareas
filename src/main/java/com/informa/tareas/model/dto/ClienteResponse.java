package com.informa.tareas.model.dto;

import java.util.Date;

import lombok.Data;

/**
 *
 * @author Hector Leon
 */
@Data
public class ClienteResponse {
  private Long id;
  private String nombres;
  private String apellidos;
  private String identificacion;
  private String sexo;
  private String telefono;
  private String celular;
  private String direccion;
  private String correo;
  private Date fechaNacimiento;
  private String tipoIdentificacion;
}
