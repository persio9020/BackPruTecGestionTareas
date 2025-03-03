package com.mainsoft.tareas.repository;

import com.mainsoft.tareas.model.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
  @Query(value = "select u from Usuario u join fetch u.usuarioRolList rl join fetch rl.rol where u.nombre = :nombre")
  Usuario findByNombre(@Param("nombre") String nombre);

}
