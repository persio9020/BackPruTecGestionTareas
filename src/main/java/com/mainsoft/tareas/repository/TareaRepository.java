package com.mainsoft.tareas.repository;

import com.mainsoft.tareas.model.entities.Tarea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TareaRepository extends JpaRepository<Tarea, Integer> {

    //@Query("SELECT * FROM tarea WHERE usuario_id = :usuarioId")
    List<Tarea> findByUsuarioId(Long usuarioId);

    @Query(
            value = "SELECT t FROM Tarea t JOIN FETCH t.estado JOIN FETCH t.usuario WHERE (t.titulo LIKE %:titulo% OR :titulo='') AND (t.descripcion LIKE %:descripcion% OR :descripcion ='') AND (t.estado.id = :estado OR :estado=0)",
            countQuery = "SELECT COUNT(t) FROM Tarea t WHERE (t.titulo LIKE %:titulo% OR :titulo='') AND (t.descripcion LIKE %:descripcion% OR :descripcion ='') AND (t.estado.id = :estado OR :estado=0)"
    )
    Page<Tarea> findTareasByFilters(@Param("titulo") String titulo,
                                    @Param("descripcion") String descripcion,
                                    @Param("estado") Short estado,
                                    Pageable pageable);


    @Query(
            value = "SELECT t FROM Tarea t JOIN FETCH t.estado JOIN FETCH t.usuario WHERE (t.titulo LIKE %:titulo% OR :titulo='') AND (t.descripcion LIKE %:descripcion% OR :descripcion ='') AND (t.estado.id = :estado OR :estado=0) AND t.usuario.id=:idUsuario",
            countQuery = "SELECT COUNT(t) FROM Tarea t WHERE (t.titulo LIKE %:titulo% OR :titulo='') AND (t.descripcion LIKE %:descripcion% OR :descripcion ='') AND (t.estado.id = :estado OR :estado=0) AND t.usuario.id=:idUsuario"
    )
    Page<Tarea> findTareasByFilters(@Param("titulo") String titulo,
                                    @Param("descripcion") String descripcion,
                                    @Param("estado") Short estado,
                                    @Param("idUsuario") Long idUsuario,
                                    Pageable pageable);
}
