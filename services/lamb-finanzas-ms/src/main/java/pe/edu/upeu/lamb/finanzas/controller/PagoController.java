package pe.edu.upeu.lamb.finanzas.controller;

import pe.edu.upeu.lamb.finanzas.dto.HistorialEstadoPagoResponse;
import pe.edu.upeu.lamb.finanzas.dto.PagoRequest;
import pe.edu.upeu.lamb.finanzas.dto.PagoResponse;
import pe.edu.upeu.lamb.finanzas.dto.PagoUpdateRequest;
import pe.edu.upeu.lamb.finanzas.dto.RechazarPagoRequest;
import pe.edu.upeu.lamb.finanzas.dto.ReembolsarPagoRequest;
import pe.edu.upeu.lamb.finanzas.dto.ResumenPagoResponse;
import pe.edu.upeu.lamb.finanzas.dto.TransaccionPasarelaResponse;
import pe.edu.upeu.lamb.finanzas.entity.EstadoPago;
import pe.edu.upeu.lamb.finanzas.service.PagoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pagos")
@RequiredArgsConstructor
public class PagoController {

    private final PagoService pagoService;

    @GetMapping
    public List<PagoResponse> listar(
            @RequestParam(required = false) Long idPersona,
            @RequestParam(required = false) EstadoPago estado,
            @RequestParam(required = false) Long matriculaId) {
        return pagoService.listar(idPersona, estado, matriculaId);
    }

    @GetMapping("/resumen")
    public ResumenPagoResponse resumen(@RequestParam Long idPersona) {
        return pagoService.resumen(idPersona);
    }

    @GetMapping("/{id}")
    public PagoResponse obtener(@PathVariable Long id) {
        return pagoService.obtener(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PagoResponse procesar(@Valid @RequestBody PagoRequest request) {
        return pagoService.procesar(request);
    }

    @PutMapping("/{id}")
    public PagoResponse actualizar(@PathVariable Long id, @Valid @RequestBody PagoUpdateRequest request) {
        return pagoService.actualizar(id, request);
    }

    @PostMapping("/{id}/aprobar")
    public PagoResponse aprobar(@PathVariable Long id) {
        return pagoService.aprobar(id);
    }

    @PostMapping("/{id}/rechazar")
    public PagoResponse rechazar(@PathVariable Long id, @RequestBody(required = false) RechazarPagoRequest request) {
        return pagoService.rechazar(id, request);
    }

    @PostMapping("/{id}/reembolsar")
    public PagoResponse reembolsar(@PathVariable Long id, @RequestBody(required = false) ReembolsarPagoRequest request) {
        return pagoService.reembolsar(id, request);
    }

    @GetMapping("/{id}/historial")
    public List<HistorialEstadoPagoResponse> historial(@PathVariable Long id) {
        return pagoService.historial(id);
    }

    @GetMapping("/{id}/transacciones")
    public List<TransaccionPasarelaResponse> transacciones(@PathVariable Long id) {
        return pagoService.transacciones(id);
    }
}
