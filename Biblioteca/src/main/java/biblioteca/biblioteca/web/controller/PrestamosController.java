package biblioteca.biblioteca.web.controller;

import biblioteca.biblioteca.application.command.PrestarCopiaCommand;
import biblioteca.biblioteca.application.command.PrestarCopiaCommandHandler;
import biblioteca.biblioteca.web.dto.PrestamoDto;
import biblioteca.biblioteca.web.mapper.PrestamoDtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/prestamos")
@RequiredArgsConstructor
public class PrestamosController {

    private final PrestarCopiaCommandHandler prestarHandler;
    private final PrestamoDtoMapper prestamoDtoMapper;

    @GetMapping("/demo")
    public ResponseEntity<PrestamoDto> demo() {
        PrestamoDto dto = PrestamoDto.builder()
                .id(123)
                .idLector(10)
                .idCopia(55)
                .fechaInicio(LocalDate.of(2025, 1, 10))
                .fechaVencimiento(LocalDate.of(2025, 1, 31)) // +21 días aprox.
                .fechaDevolucion(null) // aún abierto
                .build();

        return ResponseEntity.ok(dto);
    }

    @PostMapping("/prestar")
    public ResponseEntity<PrestamoDto> prestar(@Valid @RequestBody PrestarCopiaCommand body) {
        var prestamo = prestarHandler.handle(body);
        var dto = prestamoDtoMapper.toDto(prestamo);

        // 201 Created + Location: /api/prestamos/{id}
        return ResponseEntity
                .created(URI.create("/api/prestamos/" + dto.getId()))
                .body(dto);
    }


}
