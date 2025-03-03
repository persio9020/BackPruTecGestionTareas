package com.mainsoft.tareas.model.dto;
import java.util.Date;
import lombok.Data;

/**
 *
 * @author Hector Leon
 */
@Data
public class ClienteRequest {
  private String nombres;
  private String apellidos;
  private String identificacion;
  private Short idSexo;
  private String telefono;
  private String celular;
  private String direccion;
  private String correo;
  private Date fechaNacimiento;
  private Short idTipoIdentificacion;
}
