package pe.edu.upeu.lamb.matricula.controller;

import pe.edu.upeu.lamb.matricula.dto.CronogramaMatriculaRequest;
import pe.edu.upeu.lamb.matricula.dto.CronogramaMatriculaResponse;
import pe.edu.upeu.lamb.matricula.service.CronogramaMatriculaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cronogramas-matricula")
@RequiredArgsConstructor
public class CronogramaMatriculaController {

    private final CronogramaMatriculaService cronogramaMatriculaService;

    @GetMapping
    public List<CronogramaMatriculaResponse> listar(@RequestParam(required = false) Long idPeriodoAcademico) {
        return cronogramaMatriculaService.listar(idPeriodoAcademico);
    }

    @GetMapping("/{id}")
    public CronogramaMatriculaResponse obtener(@PathVariable Long id) {
        return cronogramaMatriculaService.obtener(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CronogramaMatriculaResponse crear(@Valid @RequestBody CronogramaMatriculaRequest request) {
        return cronogramaMatriculaService.crear(request);
    }

    @PutMapping("/{id}")
    public CronogramaMatriculaResponse actualizar(@PathVariable Long id, @Valid @RequestBody CronogramaMatriculaRequest request) {
        return cronogramaMatriculaService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        cronogramaMatriculaService.eliminar(id);
    }
}
