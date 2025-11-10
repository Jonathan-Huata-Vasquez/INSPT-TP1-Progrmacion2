package biblioteca.biblioteca.application.command;

import biblioteca.biblioteca.domain.exception.DatoInvalidoException;
import biblioteca.biblioteca.domain.exception.ReglaDeNegocioException;
import biblioteca.biblioteca.domain.model.Copia;
import biblioteca.biblioteca.domain.model.Lector;
import biblioteca.biblioteca.domain.model.Prestamo;
import biblioteca.biblioteca.domain.port.ICopiaRepository;
import biblioteca.biblioteca.domain.port.ILectorRepository;
import biblioteca.biblioteca.domain.port.IPrestamoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * Orquesta el caso de uso "prestar copia".
 * Regla: las invariantes viven en el dominio; aquí solo coordinamos y persistimos.
 */
@Service                        // Spring: registra como bean de aplicación
@RequiredArgsConstructor        // Lombok: inyección por ctor de campos final
public class PrestarCopiaCommandHandler {

    private final ILectorRepository lectorRepo;
    private final ICopiaRepository copiaRepo;
    private final IPrestamoRepository prestamoRepo;
    // private final IAutorizacionService auth; // si luego validamos rol de idUsuario

    /**
     * Ejecuta el préstamo. Atomicidad garantizada por @Transactional.
     */
    @Transactional
    public Prestamo handle(PrestarCopiaCommand cmd) {
        if (cmd == null) throw new DatoInvalidoException("El comando no puede ser null");

        // (Opcional) Autorización de rol Bibliotecario con cmd.getIdUsuario()

        // 1) Cargar agregados
        Lector lector = lectorRepo.porId(cmd.getIdLector());
        if (lector == null) throw new DatoInvalidoException("Lector inexistente: " + cmd.getIdLector());

        Copia copia = copiaRepo.porId(cmd.getIdCopia());
        if (copia == null) throw new DatoInvalidoException("Copia inexistente: " + cmd.getIdCopia());

        // 2) Validaciones cruzadas de orquestación (defensivas)
        if (!copia.esPrestable()) {
            // Aunque el dominio revalidará al marcar prestada, avisamos temprano:
            throw new ReglaDeNegocioException("La copia no está disponible para préstamo");
        }

        LocalDate hoy = LocalDate.now();
        Prestamo nuevo = lector.abrirPrestamo(cmd.getIdCopia(), hoy);
        copia.marcarPrestada();

        Prestamo guardado = prestamoRepo.guardar(nuevo);
        lectorRepo.guardar(lector);
        copiaRepo.guardar(copia);

        return guardado;
    }
}
