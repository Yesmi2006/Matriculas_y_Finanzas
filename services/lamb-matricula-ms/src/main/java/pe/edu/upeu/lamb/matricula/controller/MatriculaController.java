package pe.edu.upeu.lamb.matricula.controller;

import pe.edu.upeu.lamb.matricula.dto.AnularMatriculaRequest;
import pe.edu.upeu.lamb.matricula.dto.CambiarSeccionRequest;
import pe.edu.upeu.lamb.matricula.dto.ConstanciaMatriculaResponse;
import pe.edu.upeu.lamb.matricula.dto.DetalleMatriculaRequest;
import pe.edu.upeu.lamb.matricula.dto.DetalleMatriculaResponse;
import pe.edu.upeu.lamb.matricula.dto.HistorialEstadoMatriculaResponse;
import pe.edu.upeu.lamb.matricula.dto.MatriculaRequest;
import pe.edu.upeu.lamb.matricula.dto.MatriculaResponse;
import pe.edu.upeu.lamb.matricula.dto.MatriculaUpdateRequest;
import pe.edu.upeu.lamb.matricula.entity.EstadoMatricula;
import pe.edu.upeu.lamb.matricula.service.MatriculaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/matriculas")
@RequiredArgsConstructor
public class MatriculaController {

    private final MatriculaService matriculaService;

    @GetMapping
    public List<MatriculaResponse> listar(
            @RequestParam(required = false) Long idPersona,
            @RequestParam(required = false) String periodo,
            @RequestParam(required = false) EstadoMatricula estado) {
        return matriculaService.listar(idPersona, periodo, estado);
    }

    @GetMapping("/{id}")
    public MatriculaResponse obtener(@PathVariable Long id) {
        return matriculaService.obtener(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MatriculaResponse crear(@Valid @RequestBody MatriculaRequest request) {
        return matriculaService.crear(request);
    }

    @PutMapping("/{id}")
    public MatriculaResponse actualizar(@PathVariable Long id, @Valid @RequestBody MatriculaUpdateRequest request) {
        return matriculaService.actualizar(id, request);
    }

    @PostMapping("/{id}/confirmar")
    public MatriculaResponse confirmar(@PathVariable Long id) {
        return matriculaService.confirmar(id);
    }

    @PostMapping("/{id}/anular")
    public MatriculaResponse anular(@PathVariable Long id, @RequestBody(required = false) AnularMatriculaRequest request) {
        return matriculaService.anular(id, request);
    }

    @GetMapping("/{id}/constancia")
    public ConstanciaMatriculaResponse constancia(@PathVariable Long id) {
        return matriculaService.constancia(id);
    }

    @GetMapping("/{id}/detalles")
    public List<DetalleMatriculaResponse> listarDetalles(@PathVariable Long id) {
        return matriculaService.listarDetalles(id);
    }

    @GetMapping("/{id}/detalles/{detalleId}")
    public DetalleMatriculaResponse obtenerDetalle(@PathVariable Long id, @PathVariable Long detalleId) {
        return matriculaService.obtenerDetalle(id, detalleId);
    }

    @PostMapping("/{id}/detalles")
    @ResponseStatus(HttpStatus.CREATED)
    public DetalleMatriculaResponse agregarDetalle(@PathVariable Long id, @Valid @RequestBody DetalleMatriculaRequest request) {
        return matriculaService.agregarDetalle(id, request);
    }

    @DeleteMapping("/{id}/detalles/{detalleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void retirarDetalle(@PathVariable Long id, @PathVariable Long detalleId) {
        matriculaService.retirarDetalle(id, detalleId);
    }

    @PutMapping("/{id}/detalles/{detalleId}/seccion")
    public DetalleMatriculaResponse cambiarSeccion(@PathVariable Long id, @PathVariable Long detalleId,
                                                    @Valid @RequestBody CambiarSeccionRequest request) {
        return matriculaService.cambiarSeccion(id, detalleId, request);
    }

    @GetMapping("/{id}/historial")
    public List<HistorialEstadoMatriculaResponse> historial(@PathVariable Long id) {
        return matriculaService.historial(id);
    }
}
