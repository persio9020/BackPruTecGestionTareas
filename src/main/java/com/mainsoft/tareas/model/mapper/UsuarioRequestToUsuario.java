package com.mainsoft.tareas.model.mapper;

import com.mainsoft.tareas.model.dto.UsuarioRequest;
import com.mainsoft.tareas.model.entities.Usuario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioRequestToUsuario {

    //@Mapping(target = "roles", source = "roles", qualifiedByName = "mapRolesToUsuarioRolList")
    Usuario map(UsuarioRequest usuarioRequest);
/*
    @Named("mapRolesToUsuarioRolList")
    default List<Rol> mapRolesToUsuarioRolList(List<RolRequest> roles) {
        return roles.stream()
                .map(rol -> {
                    Rol role = new Rol();
                    role.setId(rol.getId());
                    return role;
                })
                .collect(Collectors.toList());
    }

 */
}
