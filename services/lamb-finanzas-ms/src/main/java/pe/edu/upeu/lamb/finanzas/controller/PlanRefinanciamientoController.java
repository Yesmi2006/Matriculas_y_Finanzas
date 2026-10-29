package pe.edu.upeu.lamb.finanzas.controller;

import pe.edu.upeu.lamb.finanzas.dto.PlanRefinanciamientoRequest;
import pe.edu.upeu.lamb.finanzas.dto.PlanRefinanciamientoResponse;
import pe.edu.upeu.lamb.finanzas.service.PlanRefinanciamientoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/planes-refinanciamiento")
@RequiredArgsConstructor
public class PlanRefinanciamientoController {

    private final PlanRefinanciamientoService planRefinanciamientoService;

    @GetMapping
    public List<PlanRefinanciamientoResponse> listar(@RequestParam(required = false) Long idPersona) {
        return planRefinanciamientoService.listar(idPersona);
    }

    @GetMapping("/{id}")
    public PlanRefinanciamientoResponse obtener(@PathVariable Long id) {
        return planRefinanciamientoService.obtener(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PlanRefinanciamientoResponse crear(@Valid @RequestBody PlanRefinanciamientoRequest request) {
        return planRefinanciamientoService.crear(request);
    }

    @PostMapping("/{id}/completar")
    public PlanRefinanciamientoResponse completar(@PathVariable Long id) {
        return planRefinanciamientoService.completar(id);
    }

    @PostMapping("/{id}/cancelar")
    public PlanRefinanciamientoResponse cancelar(@PathVariable Long id) {
        return planRefinanciamientoService.cancelar(id);
    }
}
