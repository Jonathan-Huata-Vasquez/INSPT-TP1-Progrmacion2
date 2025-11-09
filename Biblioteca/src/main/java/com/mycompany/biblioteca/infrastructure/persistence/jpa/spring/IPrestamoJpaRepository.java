
package com.mycompany.biblioteca.infrastructure.persistence.jpa.spring;
import com.mycompany.biblioteca.infrastructure.persistence.jpa.entity.PrestamoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IPrestamoJpaRepository extends JpaRepository<PrestamoEntity, Integer> {

    List<PrestamoEntity> findByLectorIdAndFechaDevolucionIsNull(Integer lectorId);

    Optional<PrestamoEntity> findByLectorIdAndCopiaIdAndFechaDevolucionIsNull(Integer lectorId, Integer copiaId);
}
