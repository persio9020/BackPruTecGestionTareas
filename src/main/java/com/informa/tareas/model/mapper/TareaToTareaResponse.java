package com.informa.tareas.model.mapper;

import com.informa.tareas.model.dto.TareaResponse;
import com.informa.tareas.model.dto.TareaResponseUpdate;
import com.informa.tareas.model.entities.Tarea;
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

    @Mappings({
            @Mapping(target = "estadoId", source = "estado.id"),
            @Mapping(target = "usuarioId", source = "usuario.id")
    })
    TareaResponseUpdate mapUpdate(Tarea tarea);

}
