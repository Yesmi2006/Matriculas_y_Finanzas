package pe.edu.upeu.lamb.matricula.controller;

import pe.edu.upeu.lamb.matricula.dto.PeriodoAcademicoRequest;
import pe.edu.upeu.lamb.matricula.dto.PeriodoAcademicoResponse;
import pe.edu.upeu.lamb.matricula.entity.EstadoPeriodo;
import pe.edu.upeu.lamb.matricula.service.PeriodoAcademicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/periodos-academicos")
@RequiredArgsConstructor
public class PeriodoAcademicoController {

    private final PeriodoAcademicoService periodoAcademicoService;

    @GetMapping
    public List<PeriodoAcademicoResponse> listar(@RequestParam(required = false) EstadoPeriodo estado) {
        return periodoAcademicoService.listar(estado);
    }

    @GetMapping("/{id}")
    public PeriodoAcademicoResponse obtener(@PathVariable Long id) {
        return periodoAcademicoService.obtener(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PeriodoAcademicoResponse crear(@Valid @RequestBody PeriodoAcademicoRequest request) {
        return periodoAcademicoService.crear(request);
    }

    @PutMapping("/{id}")
    public PeriodoAcademicoResponse actualizar(@PathVariable Long id, @Valid @RequestBody PeriodoAcademicoRequest request) {
        return periodoAcademicoService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        periodoAcademicoService.eliminar(id);
    }

    @PostMapping("/{id}/cerrar")
    public PeriodoAcademicoResponse cerrar(@PathVariable Long id) {
        return periodoAcademicoService.cerrar(id);
    }

    @PostMapping("/{id}/abrir")
    public PeriodoAcademicoResponse abrir(@PathVariable Long id) {
        return periodoAcademicoService.abrir(id);
    }
}
