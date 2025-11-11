package biblioteca.biblioteca.application.command;

import biblioteca.biblioteca.application.command.PrestarCopiaCommand;
import biblioteca.biblioteca.application.exception.EntidadNoEncontradaException;
import biblioteca.biblioteca.application.exception.OperacionNoPermitidaException;
import biblioteca.biblioteca.web.dto.PrestamoDto;
import biblioteca.biblioteca.web.mapper.PrestamoDtoMapper;
import biblioteca.biblioteca.domain.exception.DatoInvalidoException;
import biblioteca.biblioteca.domain.model.Copia;
import biblioteca.biblioteca.domain.model.Lector;
import biblioteca.biblioteca.domain.model.Prestamo;
import biblioteca.biblioteca.domain.model.policy.IPoliticaPenalizacion;
import biblioteca.biblioteca.domain.port.ICopiaRepository;
import biblioteca.biblioteca.domain.port.ILectorRepository;
import biblioteca.biblioteca.domain.port.IPrestamoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PrestarCopiaCommandHandler {

    private final ILectorRepository lectorRepo;
    private final ICopiaRepository copiaRepo;
    private final IPrestamoRepository prestamoRepo;
    private final PrestamoDtoMapper dtoMapper;
    private final IPoliticaPenalizacion politica;

    @Transactional
    public PrestamoDto handle(PrestarCopiaCommand cmd) {
        if (cmd == null) throw new DatoInvalidoException("El comando no puede ser null");

        Lector lector = lectorRepo.porId(cmd.getIdLector());
        if (lector == null) throw new EntidadNoEncontradaException("Lector inexistente: " + cmd.getIdLector());

        Copia copia = copiaRepo.porId(cmd.getIdCopia());
        if (copia == null) throw new EntidadNoEncontradaException("Copia inexistente: " + cmd.getIdCopia());

        if (!copia.esPrestable()) throw new OperacionNoPermitidaException("La copia no está disponible para préstamo");
        Prestamo yaActivo = prestamoRepo.activoPor(cmd.getIdLector(), cmd.getIdCopia());
        if (yaActivo != null) throw new OperacionNoPermitidaException("Ya existe un préstamo activo para esa copia y lector");

        var hoy = LocalDate.now();
        Prestamo nuevo = lector.abrirPrestamo(cmd.getIdCopia(), hoy, politica);
        copia.marcarPrestada();

        Prestamo guardado = prestamoRepo.guardar(nuevo);
        lectorRepo.guardar(lector);
        copiaRepo.guardar(copia);

        return dtoMapper.toDto(guardado);
    }
}
