package biblioteca.biblioteca.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Entidad de dominio: Editorial
 * Regla: nombre no vacío/espacios.
 */
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE) // uso vía factorías estáticas
public class Editorial {

    @EqualsAndHashCode.Include
    @ToString.Include
    private final Integer idEditorial;

    @ToString.Include
    private String nombre;

    /* ---------- Factorías ---------- */

    public static Editorial crear(Integer idEditorial, String nombre) {
        validarId(idEditorial);
        Editorial e = new Editorial(idEditorial, null);
        e.actualizarNombre(nombre);
        return e;
    }

    /** Rehidratación desde BD. */
    public static Editorial rehidratar(Integer idEditorial, String nombre) {
        return crear(idEditorial, nombre);
    }

    /* ---------- Comportamiento ---------- */

    public void actualizarNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new DatoInvalidoException("El nombre de la editorial no puede ser vacío");
        }
        this.nombre = nombre.trim();
    }

    /* ---------- Validaciones ---------- */

    private static void validarId(Integer id) {
        if (id == null) throw new DatoInvalidoException("El id de la editorial no puede ser null");
    }
}

