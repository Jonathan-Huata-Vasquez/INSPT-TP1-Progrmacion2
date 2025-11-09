package biblioteca.biblioteca.domain.port;

import biblioteca.biblioteca.domain.model.Editorial;

public interface IEditorialRepository {
    void guardar(Editorial editorial);
    Editorial porId(Integer idEditorial);
}
