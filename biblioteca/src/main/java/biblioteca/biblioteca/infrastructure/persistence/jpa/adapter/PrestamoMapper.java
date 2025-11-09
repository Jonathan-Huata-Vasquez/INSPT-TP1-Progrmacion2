package biblioteca.biblioteca.infrastructure.persistence.jpa.adapter;



import biblioteca.biblioteca.domain.model.Prestamo;
import biblioteca.biblioteca.infrastructure.persistence.jpa.entity.PrestamoEntity;
import org.springframework.stereotype.Component;

@Component
public class PrestamoMapper {

    public Prestamo toDomain(PrestamoEntity e) {
        if (e.getFechaDevolucion() == null) {
            return Prestamo.abrir(e.getId(), e.getLectorId(), e.getCopiaId(), e.getFechaInicio(), e.getFechaVencimiento());
        } else {
            Prestamo p = Prestamo.abrir(e.getId(), e.getLectorId(), e.getCopiaId(), e.getFechaInicio(), e.getFechaVencimiento());
            p.cerrar(e.getFechaDevolucion());
            return p;
        }
    }

    public PrestamoEntity toEntity(Prestamo d) {
        return new PrestamoEntity(
            d.id(),
            d.idLector(),
            d.idCopia(),
            d.fechaInicio(),
            d.fechaVencimiento(),
            d.fechaDevolucion()
        );
    }
}