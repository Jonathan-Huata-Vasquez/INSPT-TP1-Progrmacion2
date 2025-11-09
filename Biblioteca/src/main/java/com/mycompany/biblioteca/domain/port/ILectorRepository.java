package com.mycompany.biblioteca.domain.port;

import com.mycompany.biblioteca.domain.model.Lector;
import java.time.LocalDate;
import java.util.List;

public interface ILectorRepository {
    void guardar(Lector lector);
    Lector porId(Integer idLector);
    List<Lector> lectoresBloqueados(LocalDate hastaFecha);
}
