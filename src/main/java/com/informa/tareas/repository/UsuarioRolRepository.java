package com.informa.tareas.repository;

import com.informa.tareas.model.entities.UsuarioRol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UsuarioRolRepository extends JpaRepository<UsuarioRol, Long> {
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO usuario_rol (usuario_id, rol_id) VALUES (:usuarioId, :rolId)", nativeQuery = true)
    void insertarUsuarioRol(@Param("usuarioId") Long usuarioId, @Param("rolId") Short rolId);
}
