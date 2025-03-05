package com.informa.tareas.repository;

import com.informa.tareas.model.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
  @Query(value = "select u from Usuario u join fetch u.usuarioRolList rl join fetch rl.rol where u.nombre = :nombre OR u.correo=:nombre")
  List<Usuario> findByNombre(@Param("nombre") String nombre);

}
