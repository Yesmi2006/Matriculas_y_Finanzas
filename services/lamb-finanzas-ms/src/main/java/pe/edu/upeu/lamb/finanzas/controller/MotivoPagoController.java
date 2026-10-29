package pe.edu.upeu.lamb.finanzas.controller;

import pe.edu.upeu.lamb.finanzas.dto.MotivoPagoRequest;
import pe.edu.upeu.lamb.finanzas.dto.MotivoPagoResponse;
import pe.edu.upeu.lamb.finanzas.entity.TipoMotivoPago;
import pe.edu.upeu.lamb.finanzas.service.MotivoPagoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/motivos-pago")
@RequiredArgsConstructor
public class MotivoPagoController {

    private final MotivoPagoService motivoPagoService;

    @GetMapping
    public List<MotivoPagoResponse> listar(
            @RequestParam(required = false) TipoMotivoPago tipo,
            @RequestParam(required = false) Boolean vigente) {
        return motivoPagoService.listar(tipo, vigente);
    }

    @GetMapping("/{id}")
    public MotivoPagoResponse obtener(@PathVariable Long id) {
        return motivoPagoService.obtener(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MotivoPagoResponse crear(@Valid @RequestBody MotivoPagoRequest request) {
        return motivoPagoService.crear(request);
    }

    @PutMapping("/{id}")
    public MotivoPagoResponse actualizar(@PathVariable Long id, @Valid @RequestBody MotivoPagoRequest request) {
        return motivoPagoService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        motivoPagoService.eliminar(id);
    }
}
