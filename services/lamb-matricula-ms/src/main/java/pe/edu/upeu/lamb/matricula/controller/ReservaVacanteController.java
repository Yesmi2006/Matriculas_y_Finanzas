package pe.edu.upeu.lamb.matricula.controller;

import pe.edu.upeu.lamb.matricula.dto.ReservaVacanteRequest;
import pe.edu.upeu.lamb.matricula.dto.ReservaVacanteResponse;
import pe.edu.upeu.lamb.matricula.entity.EstadoReserva;
import pe.edu.upeu.lamb.matricula.service.ReservaVacanteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reservas-vacante")
@RequiredArgsConstructor
public class ReservaVacanteController {

    private final ReservaVacanteService reservaVacanteService;

    @GetMapping
    public List<ReservaVacanteResponse> listar(
            @RequestParam(required = false) Long idPersona,
            @RequestParam(required = false) Long idSeccion,
            @RequestParam(required = false) EstadoReserva estado) {
        return reservaVacanteService.listar(idPersona, idSeccion, estado);
    }

    @GetMapping("/{id}")
    public ReservaVacanteResponse obtener(@PathVariable Long id) {
        return reservaVacanteService.obtener(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservaVacanteResponse crear(@Valid @RequestBody ReservaVacanteRequest request) {
        return reservaVacanteService.crear(request);
    }

    @PostMapping("/{id}/cancelar")
    public ReservaVacanteResponse cancelar(@PathVariable Long id) {
        return reservaVacanteService.cancelar(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        reservaVacanteService.eliminar(id);
    }
}
