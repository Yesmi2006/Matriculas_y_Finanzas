package pe.edu.upeu.lamb.matricula.controller;

import pe.edu.upeu.lamb.matricula.dto.SolicitudExcepcionRequest;
import pe.edu.upeu.lamb.matricula.dto.SolicitudExcepcionResponse;
import pe.edu.upeu.lamb.matricula.dto.SolicitudExcepcionUpdateRequest;
import pe.edu.upeu.lamb.matricula.entity.EstadoSolicitudExcepcion;
import pe.edu.upeu.lamb.matricula.service.SolicitudExcepcionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/solicitudes-excepcion")
@RequiredArgsConstructor
public class SolicitudExcepcionController {

    private final SolicitudExcepcionService solicitudExcepcionService;

    @GetMapping
    public List<SolicitudExcepcionResponse> listar(
            @RequestParam(required = false) Long idPersona,
            @RequestParam(required = false) EstadoSolicitudExcepcion estado) {
        return solicitudExcepcionService.listar(idPersona, estado);
    }

    @GetMapping("/{id}")
    public SolicitudExcepcionResponse obtener(@PathVariable Long id) {
        return solicitudExcepcionService.obtener(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SolicitudExcepcionResponse crear(@Valid @RequestBody SolicitudExcepcionRequest request) {
        return solicitudExcepcionService.crear(request);
    }

    @PutMapping("/{id}")
    public SolicitudExcepcionResponse actualizar(@PathVariable Long id, @Valid @RequestBody SolicitudExcepcionUpdateRequest request) {
        return solicitudExcepcionService.actualizar(id, request);
    }

    @PostMapping("/{id}/aprobar")
    public SolicitudExcepcionResponse aprobar(@PathVariable Long id) {
        return solicitudExcepcionService.aprobar(id);
    }

    @PostMapping("/{id}/rechazar")
    public SolicitudExcepcionResponse rechazar(@PathVariable Long id) {
        return solicitudExcepcionService.rechazar(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        solicitudExcepcionService.eliminar(id);
    }
}
