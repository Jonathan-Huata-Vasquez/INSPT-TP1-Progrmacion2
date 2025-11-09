/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.biblioteca.infrastructure.persistence.jpa.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "prestamo")
public class PrestamoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "lector_id", nullable = false)
    private Integer lectorId;

    @Column(name = "copia_id", nullable = false)
    private Integer copiaId;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_vencimiento", nullable = false)
    private LocalDate fechaVencimiento;

    @Column(name = "fecha_devolucion")
    private LocalDate fechaDevolucion; // null = activo

    protected PrestamoEntity() {
    }

    public PrestamoEntity(Integer id, Integer lectorId, Integer copiaId,
            LocalDate fechaInicio, LocalDate fechaVencimiento, LocalDate fechaDevolucion) {
        this.id = id;
        this.lectorId = lectorId;
        this.copiaId = copiaId;
        this.fechaInicio = fechaInicio;
        this.fechaVencimiento = fechaVencimiento;
        this.fechaDevolucion = fechaDevolucion;
    }

    // Getters/Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLectorId() {
        return lectorId;
    }

    public void setLectorId(Integer lectorId) {
        this.lectorId = lectorId;
    }

    public Integer getCopiaId() {
        return copiaId;
    }

    public void setCopiaId(Integer copiaId) {
        this.copiaId = copiaId;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(LocalDate fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }
}
