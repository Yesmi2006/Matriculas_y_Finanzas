package pe.edu.upeu.lamb.finanzas.controller;

import pe.edu.upeu.lamb.finanzas.dto.ReporteCuotasVencidasResponse;
import pe.edu.upeu.lamb.finanzas.dto.ReportePorMetodoResponse;
import pe.edu.upeu.lamb.finanzas.service.ReportePagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pagos/reportes")
@RequiredArgsConstructor
public class ReportePagoController {

    private final ReportePagoService reportePagoService;

    @GetMapping("/por-metodo")
    public List<ReportePorMetodoResponse> porMetodo() {
        return reportePagoService.porMetodo();
    }

    @GetMapping("/cuotas-vencidas")
    public ReporteCuotasVencidasResponse cuotasVencidas() {
        return reportePagoService.cuotasVencidas();
    }
}
