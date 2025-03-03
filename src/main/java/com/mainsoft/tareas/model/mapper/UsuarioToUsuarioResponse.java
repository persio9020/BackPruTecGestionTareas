package com.mainsoft.tareas.model.mapper;

import com.mainsoft.tareas.model.dto.UsuarioResponse;
import com.mainsoft.tareas.model.entities.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioToUsuarioResponse {

    @Mapping(target="contrasenia", ignore=true)
    UsuarioResponse map(Usuario usuario);
}
