package pe.edu.upeu.lamb.finanzas.controller;

import pe.edu.upeu.lamb.finanzas.dto.TransaccionPasarelaResponse;
import pe.edu.upeu.lamb.finanzas.service.TransaccionPasarelaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transacciones-pasarela")
@RequiredArgsConstructor
public class TransaccionPasarelaController {

    private final TransaccionPasarelaService transaccionPasarelaService;

    @GetMapping
    public List<TransaccionPasarelaResponse> listar(@RequestParam Long idPago) {
        return transaccionPasarelaService.listarPorPago(idPago);
    }
}
