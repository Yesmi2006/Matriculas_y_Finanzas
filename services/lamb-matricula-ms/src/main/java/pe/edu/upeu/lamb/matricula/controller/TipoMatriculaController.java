package pe.edu.upeu.lamb.matricula.controller;

import pe.edu.upeu.lamb.matricula.dto.TipoMatriculaRequest;
import pe.edu.upeu.lamb.matricula.dto.TipoMatriculaResponse;
import pe.edu.upeu.lamb.matricula.service.TipoMatriculaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tipos-matricula")
@RequiredArgsConstructor
public class TipoMatriculaController {

    private final TipoMatriculaService tipoMatriculaService;

    @GetMapping
    public List<TipoMatriculaResponse> listar(@RequestParam(required = false) Boolean vigente) {
        return tipoMatriculaService.listar(vigente);
    }

    @GetMapping("/{id}")
    public TipoMatriculaResponse obtener(@PathVariable Long id) {
        return tipoMatriculaService.obtener(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TipoMatriculaResponse crear(@Valid @RequestBody TipoMatriculaRequest request) {
        return tipoMatriculaService.crear(request);
    }

    @PutMapping("/{id}")
    public TipoMatriculaResponse actualizar(@PathVariable Long id, @Valid @RequestBody TipoMatriculaRequest request) {
        return tipoMatriculaService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        tipoMatriculaService.eliminar(id);
    }
}
