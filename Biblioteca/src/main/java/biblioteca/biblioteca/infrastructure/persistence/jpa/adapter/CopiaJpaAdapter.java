package biblioteca.biblioteca.infrastructure.persistence.jpa.adapter;

import biblioteca.biblioteca.domain.model.Copia;
import biblioteca.biblioteca.domain.model.EstadoCopia;
import biblioteca.biblioteca.domain.port.ICopiaRepository;
import biblioteca.biblioteca.infrastructure.persistence.jpa.spring.CopiaSpringDataRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class CopiaJpaAdapter implements ICopiaRepository {

    private final CopiaSpringDataRepository repo;
    private final CopiaMapper mapper;

    public CopiaJpaAdapter(CopiaSpringDataRepository repo, CopiaMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public void guardar(Copia copia) {
        repo.save(mapper.toEntity(copia));
    }

    @Override
    @Transactional(readOnly = true)
    public Copia porId(Integer idCopia) {
        return repo.findById(idCopia).map(mapper::toDomain).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Copia> disponiblesPorLibro(Integer idLibro) {
        return repo.findByLibroIdAndEstado(idLibro, EstadoCopia.EnBiblioteca)
                .stream().map(mapper::toDomain).toList();
    }
}
