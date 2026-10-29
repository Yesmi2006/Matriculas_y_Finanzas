package pe.edu.upeu.lamb.finanzas.controller;

import pe.edu.upeu.lamb.finanzas.dto.DocumentoPagoRequest;
import pe.edu.upeu.lamb.finanzas.dto.DocumentoPagoResponse;
import pe.edu.upeu.lamb.finanzas.service.DocumentoPagoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/documentos-pago")
@RequiredArgsConstructor
public class DocumentoPagoController {

    private final DocumentoPagoService documentoPagoService;

    @GetMapping
    public List<DocumentoPagoResponse> listar(@RequestParam(required = false) Long idPago) {
        return documentoPagoService.listar(idPago);
    }

    @GetMapping("/{id}")
    public DocumentoPagoResponse obtener(@PathVariable Long id) {
        return documentoPagoService.obtener(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DocumentoPagoResponse crear(@Valid @RequestBody DocumentoPagoRequest request) {
        return documentoPagoService.crear(request);
    }

    @PostMapping("/{id}/anular")
    public DocumentoPagoResponse anular(@PathVariable Long id) {
        return documentoPagoService.anular(id);
    }
}
