
package com.mycompany.biblioteca.infrastructure.persistence.jpa.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "lector")
public class LectorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nombre;

    @Column(name = "bloqueado_hasta")
    private LocalDate bloqueadoHasta; // null = no bloqueado

    protected LectorEntity() {}

    public LectorEntity(Integer id, String nombre, LocalDate bloqueadoHasta) {
        this.id = id;
        this.nombre = nombre;
        this.bloqueadoHasta = bloqueadoHasta;
    }

    // Getters/Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public LocalDate getBloqueadoHasta() { return bloqueadoHasta; }
    public void setBloqueadoHasta(LocalDate bloqueadoHasta) { this.bloqueadoHasta = bloqueadoHasta; }
}