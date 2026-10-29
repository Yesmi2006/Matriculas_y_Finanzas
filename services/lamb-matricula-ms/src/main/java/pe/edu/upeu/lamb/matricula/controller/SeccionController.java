package pe.edu.upeu.lamb.matricula.controller;

import pe.edu.upeu.lamb.matricula.dto.SeccionRequest;
import pe.edu.upeu.lamb.matricula.dto.SeccionResponse;
import pe.edu.upeu.lamb.matricula.entity.EstadoSeccion;
import pe.edu.upeu.lamb.matricula.service.SeccionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/secciones")
@RequiredArgsConstructor
public class SeccionController {

    private final SeccionService seccionService;

    @GetMapping
    public List<SeccionResponse> listar(
            @RequestParam(required = false) Long idPeriodoAcademico,
            @RequestParam(required = false) EstadoSeccion estado) {
        return seccionService.listar(idPeriodoAcademico, estado);
    }

    @GetMapping("/{id}")
    public SeccionResponse obtener(@PathVariable Long id) {
        return seccionService.obtener(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SeccionResponse crear(@Valid @RequestBody SeccionRequest request) {
        return seccionService.crear(request);
    }

    @PutMapping("/{id}")
    public SeccionResponse actualizar(@PathVariable Long id, @Valid @RequestBody SeccionRequest request) {
        return seccionService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        seccionService.eliminar(id);
    }

    @PostMapping("/{id}/cerrar")
    public SeccionResponse cerrar(@PathVariable Long id) {
        return seccionService.cerrar(id);
    }

    @PostMapping("/{id}/abrir")
    public SeccionResponse abrir(@PathVariable Long id) {
        return seccionService.abrir(id);
    }
}
