package biblioteca.biblioteca.domain.model;

import biblioteca.biblioteca.domain.exception.DatoInvalidoException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Entidad de dominio: Libro
 * Reglas:
 *  - titulo no vacío
 *  - anioPublicacion > 0 (y opcionalmente <= año actual)
 *  - idAutor e idEditorial obligatorios
 *  - categoria obligatoria
 */
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE) // factorías controlan creación/validación
public class Libro {

    @EqualsAndHashCode.Include
    @ToString.Include
    private final Integer idLibro;

    @ToString.Include
    private String titulo;

    private Integer anioPublicacion;

    private Integer idAutor;

    private Integer idEditorial;

    private Categoria categoria;

    public static Libro crear(Integer idLibro, String titulo, Integer anioPublicacion,
                              Integer idAutor, Integer idEditorial, Categoria categoria) {
        validarId(idLibro);
        Libro l = new Libro(idLibro, null, null, null, null, null);
        l.actualizarMetadatos(titulo, anioPublicacion, idAutor, idEditorial, categoria);
        return l;
    }

    public static Libro rehidratar(Integer idLibro, String titulo, Integer anioPublicacion,
                                   Integer idAutor, Integer idEditorial, Categoria categoria) {
        return crear(idLibro, titulo, anioPublicacion, idAutor, idEditorial, categoria);
    }

    /* ---------- Comportamiento ---------- */
    public void actualizarMetadatos(String titulo, Integer anioPublicacion,
                                    Integer idAutor, Integer idEditorial, Categoria categoria) {
        this.titulo = validarTitulo(titulo);
        this.anioPublicacion = validarAnio(anioPublicacion);
        this.idAutor = validarObligatorio(idAutor, "idAutor");
        this.idEditorial = validarObligatorio(idEditorial, "idEditorial");
        if (categoria == null) throw new DatoInvalidoException("La categoría es obligatoria");
        this.categoria = categoria;
    }

    /* ---------- Validaciones ---------- */
    private static void validarId(Integer id) {
        if (id == null) throw new DatoInvalidoException("El id del libro no puede ser null");
    }

    private static String validarTitulo(String t) {
        if (t == null || t.trim().isEmpty()) throw new DatoInvalidoException("El título no puede ser vacío");
        return t.trim();
    }

    private static Integer validarAnio(Integer anio) {
        if (anio == null || anio <= 0) throw new DatoInvalidoException("El año de publicación debe ser positivo");
        // opcional: validar contra el año actual
        // if (anio > LocalDate.now().getYear()) throw new DatoInvalidoException("El año de publicación no puede ser futuro");
        return anio;
    }

    private static Integer validarObligatorio(Integer v, String nombre) {
        if (v == null) throw new DatoInvalidoException(nombre + " no puede ser null");
        return v;
    }
}
