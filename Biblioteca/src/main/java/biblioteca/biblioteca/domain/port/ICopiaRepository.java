package biblioteca.biblioteca.domain.port;

import biblioteca.biblioteca.domain.model.Copia;

import java.util.List;

public interface ICopiaRepository {
    void guardar(Copia copia);
    Copia porId(Integer idCopia);
    List<Copia> disponiblesPorLibro(Integer idLibro); // típicamente estado == EnBiblioteca
}
