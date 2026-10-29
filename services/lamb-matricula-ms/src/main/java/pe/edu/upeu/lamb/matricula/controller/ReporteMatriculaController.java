package pe.edu.upeu.lamb.matricula.controller;

import pe.edu.upeu.lamb.matricula.dto.ReporteResumenPeriodoResponse;
import pe.edu.upeu.lamb.matricula.dto.ReporteSeccionResponse;
import pe.edu.upeu.lamb.matricula.service.ReporteMatriculaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/matriculas/reportes")
@RequiredArgsConstructor
public class ReporteMatriculaController {

    private final ReporteMatriculaService reporteMatriculaService;

    @GetMapping("/por-seccion")
    public ReporteSeccionResponse porSeccion(@RequestParam Long idSeccion) {
        return reporteMatriculaService.porSeccion(idSeccion);
    }

    @GetMapping("/resumen")
    public ReporteResumenPeriodoResponse resumen(@RequestParam String periodo) {
        return reporteMatriculaService.resumenPorPeriodo(periodo);
    }
}
