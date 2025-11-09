package biblioteca.biblioteca.domain.port;

import biblioteca.biblioteca.domain.model.Libro;

import java.util.List;

public interface ILibroRepository {
    void guardar(Libro libro);
    Libro porId(Integer idLibro);
    List<Libro> buscarPorAutor(Integer idAutor);
}
