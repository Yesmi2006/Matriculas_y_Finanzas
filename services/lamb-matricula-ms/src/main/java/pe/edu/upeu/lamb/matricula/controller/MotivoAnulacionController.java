package pe.edu.upeu.lamb.matricula.controller;

import pe.edu.upeu.lamb.matricula.dto.MotivoAnulacionRequest;
import pe.edu.upeu.lamb.matricula.dto.MotivoAnulacionResponse;
import pe.edu.upeu.lamb.matricula.service.MotivoAnulacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/motivos-anulacion")
@RequiredArgsConstructor
public class MotivoAnulacionController {

    private final MotivoAnulacionService motivoAnulacionService;

    @GetMapping
    public List<MotivoAnulacionResponse> listar(@RequestParam(required = false) Boolean vigente) {
        return motivoAnulacionService.listar(vigente);
    }

    @GetMapping("/{id}")
    public MotivoAnulacionResponse obtener(@PathVariable Long id) {
        return motivoAnulacionService.obtener(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MotivoAnulacionResponse crear(@Valid @RequestBody MotivoAnulacionRequest request) {
        return motivoAnulacionService.crear(request);
    }

    @PutMapping("/{id}")
    public MotivoAnulacionResponse actualizar(@PathVariable Long id, @Valid @RequestBody MotivoAnulacionRequest request) {
        return motivoAnulacionService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        motivoAnulacionService.eliminar(id);
    }
}
