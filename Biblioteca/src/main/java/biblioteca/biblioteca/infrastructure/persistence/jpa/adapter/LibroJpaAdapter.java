package biblioteca.biblioteca.infrastructure.persistence.jpa.adapter;

import biblioteca.biblioteca.domain.model.Libro;
import biblioteca.biblioteca.domain.port.ILibroRepository;
import biblioteca.biblioteca.infrastructure.persistence.jpa.entity.LibroEntity;
import biblioteca.biblioteca.infrastructure.persistence.jpa.spring.LibroSpringDataRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class LibroJpaAdapter implements ILibroRepository {

    private final LibroSpringDataRepository repo;
    private final LibroMapper mapper;

    public LibroJpaAdapter(LibroSpringDataRepository repo, LibroMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public void guardar(Libro libro) {
        LibroEntity e = mapper.toEntity(libro);
        repo.save(e);
    }

    @Override
    @Transactional(readOnly = true)
    public Libro porId(Integer idLibro) {
        return repo.findById(idLibro).map(mapper::toDomain).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Libro> buscarPorAutor(Integer idAutor) {
        return repo.findByAutorId(idAutor).stream().map(mapper::toDomain).toList();
    }
}
