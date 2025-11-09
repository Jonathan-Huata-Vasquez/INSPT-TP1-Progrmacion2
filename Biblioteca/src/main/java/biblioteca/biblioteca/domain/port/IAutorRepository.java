package biblioteca.biblioteca.domain.port;

import biblioteca.biblioteca.domain.model.Autor;

public interface IAutorRepository {
    void guardar(Autor autor);
    Autor porId(Integer idAutor);
}
