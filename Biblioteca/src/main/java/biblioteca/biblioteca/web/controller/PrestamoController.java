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

@RestController
@RequestMapping("/api/prestamo")
@RequiredArgsConstructor
public class PrestamoController {

    private final PrestarCopiaCommandHandler prestarHandler;
    private final PrestamoDtoMapper prestamoDtoMapper;

    @PostMapping("/prestar")
    public ResponseEntity<PrestamoDto> prestar(@Valid @RequestBody PrestarCopiaCommand body) {
        var prestamo = prestarHandler.handle(body);
        var dto = prestamoDtoMapper.toDto(prestamo);

        // 201 Created + Location: /api/prestamos/{id}
        return ResponseEntity
                .created(URI.create("/api/prestamo/" + dto.getId()))
                .body(dto);
    }
}
