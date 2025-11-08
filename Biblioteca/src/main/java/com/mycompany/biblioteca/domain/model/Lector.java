package com.mycompany.biblioteca.domain.model;


import java.util.Objects;

/**
 * Agregado: Lector
 * Identidad: id (Long, asignado por la BD en infraestructura)
 * Invariantes:
 *  - Nace ACTIVO con 0 préstamos activos.
 *  - No puede superar 5 préstamos activos.
 *  - Si está BLOQUEADO no puede pedir préstamos.
 */
public class Lector {

    private Long id;                 // asignado por la BD (IDENTITY/SEQUENCE)
    private String nombre;           // obligatorio (validado en la fábrica)
    private EstadoLector estado;     // p.ej., ACTIVO por defecto
    private int prestamosActivos;    // empieza en 0

    // Requerido por ORMs (p.ej., JPA). No usar directamente en el dominio.
    protected Lector() { }

    // Constructor privado: garantiza defaults e invariantes de creación.
    private Lector(String nombreNormalizado) {
        this.id = null;                         // lo completará la BD
        this.nombre = nombreNormalizado;        // ya validado/normalizado
        this.estado = EstadoLector.ACTIVO;      // default de negocio
        this.prestamosActivos = 0;              // sin préstamos al crear
    }

    // Fábrica estática: única vía para crear un Lector válido.
    public static Lector crear(String nombre) {
        if (nombre == null) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        String v = nombre.trim();
        if (v.isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if (v.length() > 120) {
            throw new IllegalArgumentException("El nombre es demasiado largo");
        }
        return new Lector(v);
    }
}