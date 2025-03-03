package com.mainsoft.tareas.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "rol")
public class Rol {
    @Id
    @Column(name = "id", nullable = false)
    private Short id;

    @Size(max = 255)
    @Column(name = "nombre")
    private String nombre;
}