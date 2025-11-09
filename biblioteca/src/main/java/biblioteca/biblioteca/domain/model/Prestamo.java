package biblioteca.biblioteca.domain.model;


import biblioteca.biblioteca.domain.exception.DatoInvalidoException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class Prestamo {

    private final Integer idPrestamo;      // puede ser null al abrir en memoria
    private final Integer idLector;
    private final Integer idCopia;
    private final LocalDate fechaInicio;
    private final LocalDate fechaVencimiento;
    private LocalDate fechaDevolucion;     // null = abierto

    private Prestamo(Integer idPrestamo, Integer idLector, Integer idCopia,
            LocalDate inicio, LocalDate vencimiento, LocalDate devolucion) {
        this.idPrestamo = idPrestamo;
        this.idLector = Objects.requireNonNull(idLector, "idLector requerido");
        this.idCopia = Objects.requireNonNull(idCopia, "idCopia requerido");
        this.fechaInicio = Objects.requireNonNull(inicio, "fechaInicio requerida");
        this.fechaVencimiento = Objects.requireNonNull(vencimiento, "fechaVencimiento requerida");
        this.fechaDevolucion = devolucion; // puede ser null
    }

    public static Prestamo abrir(Integer idPrestamo, Integer idLector, Integer idCopia,
            LocalDate inicio, LocalDate vencimiento) {
        if (vencimiento.isBefore(inicio)) {
            throw new DatoInvalidoException("Vencimiento antes del inicio");
        }
        return new Prestamo(idPrestamo, idLector, idCopia, inicio, vencimiento, null);
    }

    public boolean estaAbierto() {
        return fechaDevolucion == null;
    }

    /**
     * Días de atraso "al" momento indicado. Si el préstamo ya está cerrado,
     * retorna el atraso definitivo (ignora fechaReferencia).
     */
    public int diasAtrasoAl(LocalDate fechaReferencia) {
        if (fechaReferencia == null) {
            throw new DatoInvalidoException("fechaReferencia no puede ser null");
        }
        if (fechaDevolucion != null) {
            return diasAtrasoDefinitivo();
        }
        long diff = ChronoUnit.DAYS.between(fechaVencimiento, fechaReferencia);
        return (int) Math.max(0, diff);
    }

    /**
     * Días de atraso definitivos del préstamo (cálculo con fechaDevolucion). Si
     * aún está abierto, retorna 0.
     */
    public int diasAtrasoDefinitivo() {
        if (fechaDevolucion == null) {
            return 0;
        }
        long diff = ChronoUnit.DAYS.between(fechaVencimiento, fechaDevolucion);
        return (int) Math.max(0, diff);
    }

    public void cerrar(LocalDate fechaDevolucion) {
        if (fechaDevolucion == null) {
            throw new DatoInvalidoException("Fecha de devolución requerida");
        }
        if (this.fechaDevolucion != null) {
            throw new DatoInvalidoException("El préstamo ya está cerrado");
        }
        this.fechaDevolucion = fechaDevolucion;
    }

    // Getters usados por Lector y mappers
    public Integer id() {
        return idPrestamo;
    }

    public Integer idLector() {
        return idLector;
    }

    public Integer idCopia() {
        return idCopia;
    }

    public LocalDate fechaInicio() {
        return fechaInicio;
    }

    public LocalDate fechaVencimiento() {
        return fechaVencimiento;
    }

    public LocalDate fechaDevolucion() {
        return fechaDevolucion;
    }
}
