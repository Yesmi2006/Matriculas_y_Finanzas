package pe.edu.upeu.lamb.finanzas.controller;

import pe.edu.upeu.lamb.finanzas.dto.MetodoPagoConfigRequest;
import pe.edu.upeu.lamb.finanzas.dto.MetodoPagoConfigResponse;
import pe.edu.upeu.lamb.finanzas.service.MetodoPagoConfigService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/metodos-pago")
@RequiredArgsConstructor
public class MetodoPagoConfigController {

    private final MetodoPagoConfigService metodoPagoConfigService;

    @GetMapping
    public List<MetodoPagoConfigResponse> listar(@RequestParam(required = false) Boolean vigente) {
        return metodoPagoConfigService.listar(vigente);
    }

    @GetMapping("/{id}")
    public MetodoPagoConfigResponse obtener(@PathVariable Long id) {
        return metodoPagoConfigService.obtener(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MetodoPagoConfigResponse crear(@Valid @RequestBody MetodoPagoConfigRequest request) {
        return metodoPagoConfigService.crear(request);
    }

    @PutMapping("/{id}")
    public MetodoPagoConfigResponse actualizar(@PathVariable Long id, @Valid @RequestBody MetodoPagoConfigRequest request) {
        return metodoPagoConfigService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        metodoPagoConfigService.eliminar(id);
    }
}
