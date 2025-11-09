
package com.mycompany.biblioteca.infrastructure.persistence.jpa.spring;

import com.mycompany.biblioteca.infrastructure.persistence.jpa.entity.LectorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDate;
import java.util.List;

public interface ILectorJpaRepository extends JpaRepository<LectorEntity, Integer> {

    // Lectores que siguen bloqueados en 'hastaFecha' (hastaFecha <= bloqueado_hasta)
    @Query("SELECT l FROM LectorEntity l WHERE l.bloqueadoHasta IS NOT NULL AND l.bloqueadoHasta >= :hastaFecha")
    List<LectorEntity> bloqueadosDesde(LocalDate hastaFecha);
}