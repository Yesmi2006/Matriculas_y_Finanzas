package pe.edu.upeu.lamb.finanzas.controller;

import pe.edu.upeu.lamb.finanzas.dto.ConceptoPagoRequest;
import pe.edu.upeu.lamb.finanzas.dto.ConceptoPagoResponse;
import pe.edu.upeu.lamb.finanzas.service.ConceptoPagoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/conceptos-pago")
@RequiredArgsConstructor
public class ConceptoPagoController {

    private final ConceptoPagoService conceptoPagoService;

    @GetMapping
    public List<ConceptoPagoResponse> listar(@RequestParam(required = false) Boolean vigente) {
        return conceptoPagoService.listar(vigente);
    }

    @GetMapping("/{id}")
    public ConceptoPagoResponse obtener(@PathVariable Long id) {
        return conceptoPagoService.obtener(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ConceptoPagoResponse crear(@Valid @RequestBody ConceptoPagoRequest request) {
        return conceptoPagoService.crear(request);
    }

    @PutMapping("/{id}")
    public ConceptoPagoResponse actualizar(@PathVariable Long id, @Valid @RequestBody ConceptoPagoRequest request) {
        return conceptoPagoService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        conceptoPagoService.eliminar(id);
    }
}
