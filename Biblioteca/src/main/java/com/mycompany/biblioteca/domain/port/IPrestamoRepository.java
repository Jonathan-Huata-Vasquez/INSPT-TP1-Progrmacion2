
package com.mycompany.biblioteca.domain.port;


import com.mycompany.biblioteca.domain.model.Prestamo;
import java.util.List;

public interface IPrestamoRepository {
    void guardar(Prestamo prestamo);
    Prestamo porId(Integer idPrestamo);
    Prestamo activoPor(Integer idLector, Integer idCopia);     // null si no existe
    List<Prestamo> activosPorLector(Integer idLector);
}