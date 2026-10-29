package pe.edu.upeu.lamb.finanzas.controller;

import pe.edu.upeu.lamb.finanzas.dto.DescuentoAplicadoRequest;
import pe.edu.upeu.lamb.finanzas.dto.DescuentoAplicadoResponse;
import pe.edu.upeu.lamb.finanzas.service.DescuentoAplicadoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/descuentos")
@RequiredArgsConstructor
public class DescuentoAplicadoController {

    private final DescuentoAplicadoService descuentoAplicadoService;

    @GetMapping
    public List<DescuentoAplicadoResponse> listar(
            @RequestParam(required = false) Long idPersona,
            @RequestParam(required = false) Long idPago) {
        return descuentoAplicadoService.listar(idPersona, idPago);
    }

    @GetMapping("/{id}")
    public DescuentoAplicadoResponse obtener(@PathVariable Long id) {
        return descuentoAplicadoService.obtener(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DescuentoAplicadoResponse crear(@Valid @RequestBody DescuentoAplicadoRequest request) {
        return descuentoAplicadoService.crear(request);
    }

    @PutMapping("/{id}")
    public DescuentoAplicadoResponse actualizar(@PathVariable Long id, @Valid @RequestBody DescuentoAplicadoRequest request) {
        return descuentoAplicadoService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        descuentoAplicadoService.eliminar(id);
    }
}
