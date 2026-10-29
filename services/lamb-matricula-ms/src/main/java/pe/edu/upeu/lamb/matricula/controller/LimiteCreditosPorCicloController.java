package pe.edu.upeu.lamb.matricula.controller;

import pe.edu.upeu.lamb.matricula.dto.LimiteCreditosPorCicloRequest;
import pe.edu.upeu.lamb.matricula.dto.LimiteCreditosPorCicloResponse;
import pe.edu.upeu.lamb.matricula.service.LimiteCreditosPorCicloService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/limites-creditos")
@RequiredArgsConstructor
public class LimiteCreditosPorCicloController {

    private final LimiteCreditosPorCicloService limiteCreditosPorCicloService;

    @GetMapping
    public List<LimiteCreditosPorCicloResponse> listar() {
        return limiteCreditosPorCicloService.listar();
    }

    @GetMapping("/{id}")
    public LimiteCreditosPorCicloResponse obtener(@PathVariable Long id) {
        return limiteCreditosPorCicloService.obtener(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LimiteCreditosPorCicloResponse crear(@Valid @RequestBody LimiteCreditosPorCicloRequest request) {
        return limiteCreditosPorCicloService.crear(request);
    }

    @PutMapping("/{id}")
    public LimiteCreditosPorCicloResponse actualizar(@PathVariable Long id, @Valid @RequestBody LimiteCreditosPorCicloRequest request) {
        return limiteCreditosPorCicloService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        limiteCreditosPorCicloService.eliminar(id);
    }
}
