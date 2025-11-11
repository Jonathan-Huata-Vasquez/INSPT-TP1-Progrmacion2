package biblioteca.biblioteca.domain.model;

import biblioteca.biblioteca.domain.exception.DatoInvalidoException;
import biblioteca.biblioteca.domain.exception.ReglaDeNegocioException;
import biblioteca.biblioteca.domain.model.policy.IPoliticaPenalizacion;
import lombok.*;

import java.time.LocalDate;
import java.util.*;

@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Lector {

    @EqualsAndHashCode.Include
    @ToString.Include @Getter
    private final Integer idLector;               // puede ser null hasta persistir

    @ToString.Include @Getter
    private String nombre;

    @Getter @ToString.Include
    private LocalDate bloqueadoHasta;

    @ToString.Include
    private final List<Prestamo> prestamosActivos;

    private Lector(Integer idLector, String nombre) {
        if (idLector == null) throw new DatoInvalidoException("El id del lector no puede ser null");
        if (nombre == null || nombre.trim().isEmpty())
            throw new DatoInvalidoException("El nombre del lector no puede ser vacío ni con espacios al inicio/fin");
        this.idLector = idLector;
        this.nombre = nombre.trim();
        this.bloqueadoHasta = null;
        this.prestamosActivos = new ArrayList<>();
    }

    public static Lector nuevo(String nombre) {
        String nombreVal = validarNombre(nombre);
        return new Lector(null, nombreVal, null, new ArrayList<>());
    }

    /** >>> CAMBIO: rehidratar SIN validar “máximo de préstamos” <<< */
    public static Lector rehidratar(Integer idLector, String nombre, LocalDate bloqueadoHasta, List<Prestamo> activos) {
        Lector l = new Lector(idLector, nombre);
        l.bloqueadoHasta = bloqueadoHasta;
        if (activos != null) {
            // Solo coherencia básica: abiertos + pertenencia al lector
            for (Prestamo p : activos) {
                if (!p.estaAbierto()) {
                    throw new ReglaDeNegocioException("Solo pueden rehidratarse préstamos activos (sin fechaDevolucion)");
                }
                if (!idLector.equals(p.getIdLector())) {
                    throw new ReglaDeNegocioException(
                            "El préstamo pertenece a otro lector (esperado=" + idLector + ", actual=" + p.getIdLector() + ")"
                    );
                }
            }
            l.prestamosActivos.addAll(activos);
        }
        return l;
    }

    /** >>> CAMBIO: métodos que usan la política con getters tipo Lombok <<< */
    public Prestamo abrirPrestamo(Integer idCopia, LocalDate hoy, IPoliticaPenalizacion politica) {
        if (idCopia == null) throw new DatoInvalidoException("El id de la copia no puede ser null");
        Objects.requireNonNull(hoy, "La fecha de 'hoy' no puede ser null");
        Objects.requireNonNull(politica, "La política no puede ser null");

        if (!puedePedir(hoy, politica)) {
            if (prestamosActivos.size() >= politica.getMaxPrestamos()) {
                throw new ReglaDeNegocioException("El lector alcanzó el máximo de " + politica.getMaxPrestamos() + " préstamos activos");
            }
            throw new ReglaDeNegocioException("El lector se encuentra bloqueado hasta " + bloqueadoHasta);
        }
        if (tienePrestamoActivoDeCopia(idCopia)) {
            throw new ReglaDeNegocioException("Ya existe un préstamo activo para la misma copia (" + idCopia + ")");
        }

        LocalDate vencimiento = politica.calcularVencimiento(hoy);
        Prestamo nuevo = Prestamo.abrir(this.idLector, idCopia, hoy, vencimiento);
        prestamosActivos.add(nuevo);
        return nuevo;
    }

    public boolean puedePedir(LocalDate hoy, IPoliticaPenalizacion politica) {
        Objects.requireNonNull(hoy, "La fecha de 'hoy' no puede ser null");
        Objects.requireNonNull(politica, "La política no puede ser null");
        boolean noBloqueado = (bloqueadoHasta == null) || hoy.isAfter(bloqueadoHasta);
        boolean bajoLimite = prestamosActivos.size() < politica.getMaxPrestamos();
        return noBloqueado && bajoLimite;
    }

    public void registrarDevolucionEn(Integer idCopia, LocalDate fecha, IPoliticaPenalizacion politica) {
        if (idCopia == null || fecha == null) throw new DatoInvalidoException("idCopia y fecha no pueden ser null");
        Objects.requireNonNull(politica, "La política no puede ser null");

        Prestamo p = buscarPrestamoActivoDeCopia(idCopia);
        if (p == null) throw new ReglaDeNegocioException("No existe préstamo activo para la copia " + idCopia);

        p.cerrar(fecha);
        prestamosActivos.remove(p);

        int diasAtraso = p.diasAtrasoDefinitivo();
        if (diasAtraso > 0) {
            LocalDate nuevoBloqueo = fecha.plusDays(politica.diasBloqueoPorAtraso(diasAtraso));
            if (this.bloqueadoHasta == null || nuevoBloqueo.isAfter(this.bloqueadoHasta)) {
                this.bloqueadoHasta = nuevoBloqueo;
            }
        }
    }

    public void actualizarNombre(String nuevoNombre) { this.nombre = validarNombre(nuevoNombre); }
    public void devolver(Integer idCopia, IPoliticaPenalizacion politica) { registrarDevolucionEn(idCopia, LocalDate.now(), politica); }
    public List<Prestamo> prestamosVigentes() { return Collections.unmodifiableList(prestamosActivos); }

    private boolean tienePrestamoActivoDeCopia(Integer idCopia) {
        return prestamosActivos.stream().anyMatch(p -> Objects.equals(p.getIdCopia(), idCopia) && p.estaAbierto());
    }
    private Prestamo buscarPrestamoActivoDeCopia(Integer idCopia) {
        return prestamosActivos.stream()
                .filter(p -> Objects.equals(p.getIdCopia(), idCopia) && p.estaAbierto())
                .findFirst().orElse(null);
    }
    private static String validarNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) throw new DatoInvalidoException("El nombre del lector no puede ser vacío");
        return nombre.trim();
    }
}
