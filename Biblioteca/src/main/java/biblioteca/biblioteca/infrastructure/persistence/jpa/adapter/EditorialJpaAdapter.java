package biblioteca.biblioteca.infrastructure.persistence.jpa.adapter;

import biblioteca.biblioteca.domain.model.Editorial;
import biblioteca.biblioteca.domain.port.IEditorialRepository;
import biblioteca.biblioteca.infrastructure.persistence.jpa.entity.EditorialEntity;
import biblioteca.biblioteca.infrastructure.persistence.jpa.spring.EditorialSpringDataRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class EditorialJpaAdapter implements IEditorialRepository {

    private final EditorialSpringDataRepository repo;
    private final EditorialMapper mapper;

    public EditorialJpaAdapter(EditorialSpringDataRepository repo, EditorialMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public void guardar(Editorial editorial) {
        EditorialEntity entity = mapper.toEntity(editorial);
        repo.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Editorial porId(Integer idEditorial) {
        return repo.findById(idEditorial).map(mapper::toDomain).orElse(null);
    }
}

