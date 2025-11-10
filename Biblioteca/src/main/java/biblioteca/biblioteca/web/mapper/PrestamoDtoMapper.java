package biblioteca.biblioteca.web.mapper;

import biblioteca.biblioteca.domain.model.Prestamo;
import biblioteca.biblioteca.web.dto.PrestamoDto;
import org.springframework.stereotype.Component;

@Component
public class PrestamoDtoMapper {
    public PrestamoDto toDto(Prestamo p) {
        if (p == null) return null;
        return PrestamoDto.builder()
                .id(p.id())
                .idLector(p.idLector())
                .idCopia(p.idCopia())
                .fechaInicio(p.fechaInicio())
                .fechaVencimiento(p.fechaVencimiento())
                .fechaDevolucion(p.fechaDevolucion())
                .build();
    }
}
