package biblioteca.biblioteca.infrastructure.persistence.jpa.adapter;

import biblioteca.biblioteca.domain.model.Autor;
import biblioteca.biblioteca.domain.port.IAutorRepository;
import biblioteca.biblioteca.infrastructure.persistence.jpa.entity.AutorEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AutorJpaAdapter implements IAutorRepository {

    private final AutorJpaRepository repo;
    private final AutorMapper mapper;

    public AutorJpaAdapter(AutorJpaRepository repo, AutorMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public void guardar(Autor autor) {
        AutorEntity entity = mapper.toEntity(autor);
        repo.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Autor porId(Integer idAutor) {
        return repo.findById(idAutor).map(mapper::toDomain).orElse(null);
    }
}
