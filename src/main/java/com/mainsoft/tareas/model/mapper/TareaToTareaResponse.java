package com.mainsoft.tareas.model.mapper;

import com.mainsoft.tareas.model.dto.TareaResponse;
import com.mainsoft.tareas.model.entities.Tarea;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface TareaToTareaResponse {
    @Mappings({
            @Mapping(target = "nombreEstado", source = "estado.nombre"),
            @Mapping(target = "nombreUsuario", source = "usuario.nombre")
    })
    TareaResponse map(Tarea tarea);
}
