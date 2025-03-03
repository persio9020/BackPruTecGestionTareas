package com.mainsoft.tareas.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.HashMap;

/**
 *
 * @author persi
 */
@Data
public class BusinessRuleException extends RuntimeException  {
   
    private HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    private HashMap<String, String> errores;
    public BusinessRuleException(HttpStatus httpStatus, HashMap<String, String> errores) {
        super("Error de validación");
        this.errores = errores;
        this.httpStatus = httpStatus;
    }
}

