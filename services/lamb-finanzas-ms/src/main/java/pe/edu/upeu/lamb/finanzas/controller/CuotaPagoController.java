package pe.edu.upeu.lamb.finanzas.controller;

import pe.edu.upeu.lamb.finanzas.dto.CuotaPagoRequest;
import pe.edu.upeu.lamb.finanzas.dto.CuotaPagoResponse;
import pe.edu.upeu.lamb.finanzas.service.CuotaPagoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pagos/{idPago}/cuotas")
@RequiredArgsConstructor
public class CuotaPagoController {

    private final CuotaPagoService cuotaPagoService;

    @GetMapping
    public List<CuotaPagoResponse> listar(@PathVariable Long idPago) {
        return cuotaPagoService.listarPorPago(idPago);
    }

    @GetMapping("/{cuotaId}")
    public CuotaPagoResponse obtener(@PathVariable Long idPago, @PathVariable Long cuotaId) {
        return cuotaPagoService.obtener(idPago, cuotaId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CuotaPagoResponse crear(@PathVariable Long idPago, @Valid @RequestBody CuotaPagoRequest request) {
        return cuotaPagoService.crear(idPago, request);
    }

    @PutMapping("/{cuotaId}")
    public CuotaPagoResponse actualizar(@PathVariable Long idPago, @PathVariable Long cuotaId,
                                         @Valid @RequestBody CuotaPagoRequest request) {
        return cuotaPagoService.actualizar(idPago, cuotaId, request);
    }

    @DeleteMapping("/{cuotaId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long idPago, @PathVariable Long cuotaId) {
        cuotaPagoService.eliminar(idPago, cuotaId);
    }

    @PostMapping("/{cuotaId}/pagar")
    public CuotaPagoResponse pagar(@PathVariable Long idPago, @PathVariable Long cuotaId) {
        return cuotaPagoService.pagar(idPago, cuotaId);
    }
}
