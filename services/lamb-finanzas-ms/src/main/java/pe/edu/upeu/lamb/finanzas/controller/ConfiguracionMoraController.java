package pe.edu.upeu.lamb.finanzas.controller;

import pe.edu.upeu.lamb.finanzas.dto.ConfiguracionMoraRequest;
import pe.edu.upeu.lamb.finanzas.dto.ConfiguracionMoraResponse;
import pe.edu.upeu.lamb.finanzas.service.ConfiguracionMoraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/configuraciones-mora")
@RequiredArgsConstructor
public class ConfiguracionMoraController {

    private final ConfiguracionMoraService configuracionMoraService;

    @GetMapping
    public List<ConfiguracionMoraResponse> listar() {
        return configuracionMoraService.listar();
    }

    @GetMapping("/{id}")
    public ConfiguracionMoraResponse obtener(@PathVariable Long id) {
        return configuracionMoraService.obtener(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ConfiguracionMoraResponse crear(@Valid @RequestBody ConfiguracionMoraRequest request) {
        return configuracionMoraService.crear(request);
    }

    @PutMapping("/{id}")
    public ConfiguracionMoraResponse actualizar(@PathVariable Long id, @Valid @RequestBody ConfiguracionMoraRequest request) {
        return configuracionMoraService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        configuracionMoraService.eliminar(id);
    }
}
