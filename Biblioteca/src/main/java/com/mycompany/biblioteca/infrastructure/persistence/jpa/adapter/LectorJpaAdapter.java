
package com.mycompany.biblioteca.infrastructure.persistence.jpa.adapter;


import com.mycompany.biblioteca.domain.model.Lector;
import com.mycompany.biblioteca.domain.model.Prestamo;
import com.mycompany.biblioteca.domain.port.ILectorRepository;
import com.mycompany.biblioteca.infrastructure.persistence.jpa.entity.LectorEntity;
import com.mycompany.biblioteca.infrastructure.persistence.jpa.entity.PrestamoEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import com.mycompany.biblioteca.infrastructure.persistence.jpa.spring.ILectorJpaRepository;
import com.mycompany.biblioteca.infrastructure.persistence.jpa.spring.IPrestamoJpaRepository;

@Component
public class LectorJpaAdapter implements ILectorRepository {

    private final ILectorJpaRepository lectorRepo;
    private final IPrestamoJpaRepository prestamoRepo;
    private final LectorMapper lectorMapper;
    private final PrestamoMapper prestamoMapper;

    public LectorJpaAdapter(ILectorJpaRepository lectorRepo,
                            IPrestamoJpaRepository prestamoRepo,
                            LectorMapper lectorMapper,
                            PrestamoMapper prestamoMapper) {
        this.lectorRepo = lectorRepo;
        this.prestamoRepo = prestamoRepo;
        this.lectorMapper = lectorMapper;
        this.prestamoMapper = prestamoMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Lector porId(Integer idLector) {
        return lectorRepo.findById(idLector)
                .map(e -> {
                    List<PrestamoEntity> activos = prestamoRepo.findByLectorIdAndFechaDevolucionIsNull(e.getId());
                    List<Prestamo> domActivos = activos.stream().map(prestamoMapper::toDomain).toList();
                    return lectorMapper.toDomainWithPrestamos(e, domActivos);
                })
                .orElse(null);
    }

    @Override
    @Transactional
    public void guardar(Lector lector) {
        LectorEntity e = lectorMapper.toEntity(lector);
        lectorRepo.save(e);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Lector> lectoresBloqueados(LocalDate hastaFecha) {
        return lectorRepo.bloqueadosDesde(hastaFecha).stream()
                .map(lectorMapper::toDomain) // para listados no necesito préstamos activos
                .toList();
    }
}
