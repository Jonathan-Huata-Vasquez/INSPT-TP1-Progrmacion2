
package com.mycompany.biblioteca.infrastructure.persistence.jpa.adapter;

import com.mycompany.biblioteca.domain.model.Prestamo;
import com.mycompany.biblioteca.domain.port.IPrestamoRepository;
import com.mycompany.biblioteca.infrastructure.persistence.jpa.entity.PrestamoEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import com.mycompany.biblioteca.infrastructure.persistence.jpa.spring.IPrestamoJpaRepository;

@Component
public class PrestamoJpaAdapter implements IPrestamoRepository {

    private final IPrestamoJpaRepository repo;
    private final PrestamoMapper mapper;

    public PrestamoJpaAdapter(IPrestamoJpaRepository repo, PrestamoMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public void guardar(Prestamo prestamo) {
        PrestamoEntity entity = mapper.toEntity(prestamo);
        repo.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Prestamo porId(Integer idPrestamo) {
        return repo.findById(idPrestamo).map(mapper::toDomain).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Prestamo activoPor(Integer idLector, Integer idCopia) {
        return repo.findByLectorIdAndCopiaIdAndFechaDevolucionIsNull(idLector, idCopia)
                .map(mapper::toDomain)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Prestamo> activosPorLector(Integer idLector) {
        return repo.findByLectorIdAndFechaDevolucionIsNull(idLector)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }
}
