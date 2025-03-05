package com.informa.tareas.model.mapper;

import com.informa.tareas.model.AuthUsuarioResponse;
import com.informa.tareas.model.dto.User;
import com.informa.tareas.model.dto.UsuarioResponse;
import com.informa.tareas.model.entities.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioToUsuarioResponse {

    @Mapping(target="contrasenia", ignore=true)
    UsuarioResponse map(Usuario usuario);

    AuthUsuarioResponse map(User user);
}
