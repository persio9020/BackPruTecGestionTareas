package com.mainsoft.tareas.model.mapper;

import com.mainsoft.tareas.model.dto.TareaInsertRequest;
import com.mainsoft.tareas.model.dto.TareaUpdateRequest;
import com.mainsoft.tareas.model.entities.Tarea;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Mapper(componentModel = "spring")
public interface TareaRequestToTarea {
    @Mappings({
            @Mapping(target = "estado.id", source = "estadoId"),
            @Mapping(target = "usuario.id", source = "usuarioId")
    })
    Tarea mapInsert(TareaInsertRequest tareaUpdateRequest);

    @Mappings({
            @Mapping(target = "estado.id", source = "estadoId"),
            @Mapping(target = "usuario.id", source = "usuarioId")
    })
    Tarea mapUpdate(TareaUpdateRequest tareaUpdateRequest);

    default Date mapStringToDate(String fechaString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.parse(fechaString);
        } catch (ParseException e) {
            throw new RuntimeException("Error al parsear la fecha: " + fechaString, e);
        }
    }
}
